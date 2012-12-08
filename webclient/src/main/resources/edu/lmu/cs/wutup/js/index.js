var loadPageFunctionality = function (baseUrl, user) {
	"use strict";
    var map, infowindow,
	    calculateRadius = function (bounds) {
	        if (Number.prototype.toRad === undefined) {
	            Number.prototype.toRad = function () {
	                return this * Math.PI / 180;
	            };
	        }
	        var R = 3959, // Statute Mile
	            center = bounds.getCenter(),
	            corner = bounds.getNorthEast(),
	            lat1 = center.lat().toRad(),
	            lat2 = corner.lat().toRad(),
	            dLat = (lat2 - lat1).toRad(),
	            dLon = (corner.lng() - center.lng()).toRad(),
	            a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2),
	            c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	        return R * c;
	    },
	    generateMapAndCalendarBoundUrl = function (center, radius, start, end) {
	        return baseUrl + ':8080/wutup/occurrences?page=0&pageSize=20&center=' + center.lat() + ',' + center.lng() + '&radius=' + (radius <= 100 ? radius : 100) + '&start=' + start.getTime() +
	            '&end=' + end.getTime();
	    },
	    generateUserandCalendarBoundUrl = function (owner, start, end) {
	        return baseUrl + ':8080/wutup/events?page=0&pageSize=20&start=' + start.getTime() +
	            '&end=' + end.getTime() + '&owner=' + owner.id	;
	    },
	    
	    generateOccurrencesByUserEventIdsUrl = function (eventIds) {
	    	return baseUrl + ':8080/wutup/occurrences?page=0&pageSize=20&eventId=' + (eventIds.length === 0 ? 0 : eventIds);
	    },
	    
	    generateOccurrencesByAttendeeUrl = function (attendee) {
	    	return baseUrl + ':8080/wutup/occurrences?page=0&pageSize=20&attendee=' + attendee.id;
	    },
	    
	    generateAttendeeByOccurrenceUrl = function (occurrence) {
	    	return baseUrl + ':8080/wutup/occurrences/' + occurrence.id + '/attendees';
	    },

        displayInfoWindow = function (occurrence) {
            if (infowindow) {
                infowindow.close();
            }
            infowindow = new google.maps.InfoWindow({
                content: occurrence.event.name + '</br>',
                position: new google.maps.LatLng(occurrence.venue.latitude, occurrence.venue.longitude)
            });
            infowindow.open(map);
        },

        displayEventInfo = function (occurrence) {
        	var i, attendeesHtmlString ="";
            $("#event-info").html(function () {
                return "<h3>" + occurrence.event.name + "</h3>" + "<p>" + occurrence.event.description + "</p>" + "<h4>" + occurrence.venue.name + "</h4>" + "<p><b>Address:</b> " + occurrence.venue.address + "</p>" + "<p><b>Start:</b> " + occurrence.start + "</p>" + "<p><b>End:</b> " + occurrence.end + "</p>";
            });
            $.getJSON(generateAttendeeByOccurrenceUrl(occurrence), function (attendees) {
            	if (attendees.length > 0) { 
            		$("#attendees-header").removeClass("hidden");
	            	for (i = 0; i < attendees.length; i += 1) {
	            		 attendeesHtmlString += '<p>' + attendees[i].nickname + '</p>';
	            	}
	            	$("#event-attendees").html(attendeesHtmlString);
            	} else {
            		$("#attendees-header").addClass("hidden");
            		$("#event-attendees").html(attendeesHtmlString);
            	}
            });
        },

        createMarker = function (occurrence) {
            var marker = new google.maps.Marker({
                map: map,
                position: new google.maps.LatLng(occurrence.venue.latitude, occurrence.venue.longitude),
                title: occurrence.event.name,
                animation: google.maps.Animation.DROP
            });
            google.maps.event.addListener(marker, 'click', function () {
                displayInfoWindow(occurrence);
                displayEventInfo(occurrence);
                clearCommentList();
                displayComments(occurrence);
                armSubmitCommentButton(occurrence, user);
            });
            return marker;
        },

        populateMap = function (gMap, events, mapMarkers) {
            var i,
                deleteOverlays = function () {
                    if (mapMarkers) {
                        for (i = 0; i < mapMarkers.length; i += 1) {
                            mapMarkers[i].setMap(null);
                        }
                    }
                    mapMarkers.length = 0;
                };
            map = gMap;
            deleteOverlays();
            for (i = 0; i < events.length; i += 1) {
                mapMarkers.push(createMarker(events[i]));
            }
        },

        parseOccurrencesForCalendar = function (occurrences) {
            var calendarEvents = [],
                i;
            for (i = 0; i < occurrences.length; i += 1) {
                calendarEvents.push({
                	id: occurrences[i].id,
                    title: occurrences[i].event.name,
                    start: new Date(occurrences[i].start),
                    end: new Date(occurrences[i].end),
                    event: occurrences[i].event,
                    venue: occurrences[i].venue,
                    allDay: false
                });
            }
            return calendarEvents;
        },

        populateCalendar = function (calendarEvents) {
            $('#calendar').fullCalendar('removeEventSource', calendarEvents);
            $('#calendar').fullCalendar('removeEvents');
            $('#calendar').fullCalendar('addEventSource', calendarEvents);
        },
        
        populateHostingAndAttendingTabs = function(calendarEvents) {
        	var i,id,event,start,end,htmlString;
        	for(i = 0; i < calendarEvents.length; i += 1) {
        		id = calendarEvents[i].id
        		event = calendarEvents[i].event;
        		start = calendarEvents[i].start;
        		end = calendarEvents[i].end;
        		htmlString = "<tr><td><a href=\"./EventPage?occurrence=" + id + "&event=" + event.id + 
        		    "\">" + event.name + "</a><p>" + event.description + "</p></td><td>" +  start.toLocaleDateString() 
        		    +": " + start.toLocaleTimeString() + " - " + end.toLocaleTimeString() + "</td></tr>";
        		if (event.creator.id === user.id) {
		        	$('#hosting-event-stream > tbody:last').append(htmlString);
        		} else {
        			$('#attending-event-stream > tbody:last').append(htmlString);
        		}
        	}
        },

        createCalendar = function (calendarEvents) {
            var calendar = $('#calendar').fullCalendar({
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                theme: true,
                editable: false,
                disableDragging: true,
                dayClick: function (date, allDay) {
                    if (allDay) {
                        calendar.fullCalendar('gotoDate', date.getFullYear(), date.getMonth(), date.getDate());
                        calendar.fullCalendar('changeView', 'agendaDay');
                    }
                },
                viewDisplay: function (view) { // This function controls behavior when the calendar view is changed
                },
                eventClick: function (event) { //'event' is used here instead of 'occurrence' due to fullcalendar.js documentation
                    displayInfoWindow(event);
                    displayEventInfo(event);
                },
                events: calendarEvents
            });
        },

        getCalendarView = function () {
            return $('#calendar').fullCalendar('getView');
        },

        setUserInfo = function (user) {
        	setProfilePicture(user);
        	setUserNameForGreeting(user);
        },

        setProfilePicture = function (user) {
            if (user.facebookId !== undefined) {
                var picture = $("#profile-picture-img"),
                    facebookPictureSrc = "https://graph.facebook.com/" + user.facebookId + "/picture?type=normal";
                picture.attr("src",facebookPictureSrc);
            }
        },

        setUserNameForGreeting = function (user) {
            if (user.firstname != undefined) {
                $("#nickname").append(user.firstname);
            }
        },

        displayComments = function (occurrence) {
            requestComments(occurrence, 0);
        },

        requestComments = function(occurrence, pageNumber) {
            $.ajax({
                headers: {
                    "Accept": "application/json"
                },
                type: "GET",
                url: baseUrl + ":8080/wutup/occurrences/" + occurrence.id + "/comments?pageSize=10&page=" + pageNumber,
                async: false,
                success: function (msg) {
                    appendComments(msg);
                }
            })
        },

        appendComments = function(comments) {
            for (var i = 0; i < comments.length; i++) {
                var commentHtml = createCommentHtml(comments[i]);
                insertCommentHtml(commentHtml);
            }
        },

        createCommentHtml = function(comment) {
            var body = comment.body,
                author = comment.author.firstname + " " + comment.author.lastname,
                publishDate = new Date(comment.postdate).toString(),
                pictureSrc = getUserImageLink(comment.author);
            var newComment = $("<div class='media well'></div>");
            $(newComment).append("<div class='pull-left'><img height=30 width=30 class='media-object' src=" + pictureSrc + " /></div>"
                    + "<div class='media-body'><h5 class='media-heading'>&nbsp" + author + "</h5><p>" + body + "</p><h6>posted on "
                    + publishDate + "</h6></div>");
            return newComment;
        },

        insertCommentHtml = function (html) {
            $("#comment-list").append(html);
        },

        getUserImageLink = function (user) {
            if (user.facebookId !== undefined) {
                return "https://graph.facebook.com/" + user.facebookId + "/picture?type=square";
            }
            return "http://placehold.it/60x60";
        },

        grabCommentInput = function () {
           return $("#comment-text-field").val();
        },

        checkCommentInput = function (input) {
            return input.trim().length > 0 ? true : false;
        },

        sendCommentInput = function (occurrence, input, user) {
            var timestamp = new Date().getTime(),
                commentObject = {
                    author: user,
                    body: input,
                    postdate: timestamp
                };

            $.ajax({
                type: "POST",
                url: baseUrl + ":8080/wutup/occurrences/" + occurrence.id + "/comments",
                data: JSON.stringify(commentObject),
                contentType: "application/json",
                success: function () {
                    displayComments(occurrence);
                }
            });
        },

        clearCommentList = function() {
            $("#comment-list").empty();
        },

        armSubmitCommentButton = function (occurrence, user) {
            if (user !== undefined) {
                $("#submit-comment-btn").unbind("click").click( function () {
                    var text = grabCommentInput();
                    if (checkCommentInput(text)) {
                        $("#comment-text-field").val("");
                        sendCommentInput(occurrence, text, user);
                        clearCommentList();
                    }
                });
            }
        },

        instantiateMapAndCalendar = function () {
            var initialLocation, siberia = new google.maps.LatLng(60, 105),
                newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687),
                browserSupportFlag = false,
                calendarEvents = null,
                mapOptions = {
                    zoom: 17,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                },
                mapMarkers = [],
                handleNoGeolocation = function (errorFlag) {
                    if (errorFlag === true) {
                        alert("Geolocation service failed.");
                        initialLocation = newyork;
                    } else {
                        alert("Your browser doesn't support geolocation. We've placed you in Siberia.");
                        initialLocation = siberia;
                        infowindow = new google.maps.InfoWindow({
                            map: map,
                            position: initialLocation,
                            content: 'Here\'s what\'s going on in Siberia...'
                        });
                    }
                    map.setCenter(initialLocation);
                };
            map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
            setUserInfo(user);
            // Trying W3C Geolocation
            if (navigator.geolocation) {
                browserSupportFlag = true;
                navigator.geolocation.getCurrentPosition(function (position) {
                    initialLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                    map.setCenter(initialLocation);
                    createCalendar(null);
                    google.maps.event.addListenerOnce(map, 'idle', function () {
                    	var bounds = map.getBounds(),
	                        radius = calculateRadius(bounds),
	                        i = 0,
	                        eventIds = [];
	                    $.getJSON(generateUserandCalendarBoundUrl(user, getCalendarView().start, getCalendarView().end), function (events) { // In theory this would be by the user and not by the map and calendar bounds.
	                        for (i = 0; i < events.length; i += 1) {
	                        	eventIds.push(events[i].id);
	                        }
	                    	$.getJSON(generateOccurrencesByUserEventIdsUrl(eventIds), function (ownedOccurrences) {
	                    		$.getJSON(generateOccurrencesByAttendeeUrl(user), function (attendingOccurrences) {
	                    			var both = $.extend(ownedOccurrences, attendingOccurrences)
	                    			console.log(both);
	                    			calendarEvents = parseOccurrencesForCalendar($.extend(ownedOccurrences, attendingOccurrences));
			                        populateCalendar(calendarEvents);
			                        populateMap(map, calendarEvents, mapMarkers);
			                        populateHostingAndAttendingTabs(calendarEvents);
	                    		});
	                    	});
	                    });
                    });
                    
                }, function () {
                    handleNoGeolocation(browserSupportFlag);
                });

                // Browser doesn't support Geolocation
            } else {
                browserSupportFlag = false;
                handleNoGeolocation(browserSupportFlag);
            }

        };

    instantiateMapAndCalendar();
    //Start your Engines!

};