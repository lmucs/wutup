var loadPageFunctionality = function (baseUrl, user) {
    "use strict";
     var mapOptions = {
            zoom: 17,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        },
        map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions),
        mapMarkers = [],
        infowindow = new google.maps.InfoWindow(),
        initializeManageEventsPage = function () {
            var initialLocation, i,
            	eventIds = [],
                newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687),
                browserSupportFlag,
                input = document.getElementById('venue-address'),
                autocomplete = new google.maps.places.Autocomplete(input),
                marker = new google.maps.Marker({
                    map: map
                });
            autocomplete.bindTo('bounds', map);
            google.maps.event.addListener(autocomplete, 'place_changed', function () {
                infowindow.close();
                marker.setVisible(false);
                input.className = '';
                var place = autocomplete.getPlace(),
                    address = '';
                if (!place.geometry) {
                    // Inform the user that the place was not found and return.
                    input.className = 'notfound';
                    return;
                }
                // If the place has a geometry, then present it on a map.
                if (place.geometry.viewport) {
                    map.fitBounds(place.geometry.viewport);
                } else {
                    map.setCenter(place.geometry.location);
                    map.setZoom(15);
                    // Why 15? Because it looks good.
                }
                marker.setPosition(place.geometry.location);
                if (place.address_components) {
                    address = [((place.address_components[0] && place.address_components[0].short_name) || ''), ((place.address_components[1] && place.address_components[1].short_name) || ''), ((place.address_components[2] && place.address_components[2].short_name) || '')].join(' ');
                }
                infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
                infowindow.open(map, marker);
            });

            // Trying W3C Geolocation
            if (navigator.geolocation) {
                browserSupportFlag = true;
                navigator.geolocation.getCurrentPosition(function (position) {
                    initialLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                    map.setCenter(initialLocation);
					$.getJSON(generateUserBoundUrl(user), function (events) {
						for (i = 0; i < events.length; i += 1) {
	                        	eventIds.push(events[i].id);
	                        }
	                    $.getJSON(generateOccurrencesByUserEventIdsUrl(eventIds), function (occurrences) {
	                        console.log(occurrences);
	                        var calendarEvents = parseOccurrencesForCalendar(occurrences);
	                        createCalendar(calendarEvents);
	                        populateMap(map, occurrences, mapMarkers);
	                    });
                    });

                }, function () {
                    handleNoGeolocation(browserSupportFlag);
                });

                // Browser doesn't support Geolocation
            } else {
                browserSupportFlag = false;
                handleNoGeolocation(browserSupportFlag, newyork);
            }
            $('#facebook-sync').click(function(){
			   document.location.href=baseUrl + ':8080/wutup/auth/facebook/sync';
			});
        },

        handleNoGeolocation = function (errorFlag, fakeLocation) {
        	var initialLocation, siberia = new google.maps.LatLng(60, 105);
            if (errorFlag === true) {
                alert("Geolocation service failed.");
                console.log(map);
                initialLocation = fakeLocation;
            } else {
                alert("Your browser doesn't support geolocation. We've placed you in Siberia.");
                initialLocation = siberia;
                var infowindow = new google.maps.InfoWindow({
                    map: map,
                    position: initialLocation,
                    content: 'Here\'s what\'s going on in Siberia...'
                });
            }
            map.setCenter(initialLocation);
        },
        
        showError = function(errorText) {
        	 $('#facebook-sync').before("<div class=\"alert alert-error\"><a class=\"close\" data-dismiss=\"alert\">×</a><strong>Error: </strong>" + errorText + "</div>")
        },
	    generateUserBoundUrl = function (owner) {
	        return baseUrl + ':8080/wutup/events?page=0&pageSize=20&owner=' + owner.id	;
	    },

	    generateOccurrencesByUserEventIdsUrl = function (eventIds) {
	    	console.log(eventIds)
	    	return baseUrl + ':8080/wutup/occurrences?page=0&pageSize=20&eventId=' + (eventIds.length === 0 ? 0 : eventIds);
	    },
	    
	    removeMarker = function (markerTitle, mapMarkers) {
	    	var i;
	    	for (i = 0; i < mapMarkers.length; i += 1) {
	    		if (mapMarkers[i].title === markerTitle) {
	    			mapMarkers[i].setMap(null);
	    			return;
	    		}
	    	}
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
        displayEventInfo = function (occurrence) {
            $("#result").html(function () {
                return "<h3>" + occurrence.event.name + "</h3>" + "<p>" + occurrence.event.description + "</p>" + "<h4>" + occurrence.venue.name + "</h4>" + "<p><b>Address:</b> " + occurrence.venue.address + "</p>" + "<p><b>Start:</b> " + occurrence.start + "</p>" + "<p><b>End:</b> " + occurrence.end + "</p>";
            });

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

        parsedForUrl = function (attribute) {
            return attribute.replace(/ /g, "+");
        },

        createNewEvent = function (name, description, creator) {
            var newEvent = {
                name: name,
                description: description,
                creator: creator //TODO change to logged in user
            };
            $.ajax({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                url: baseUrl + ':8080/wutup/events',
                type: 'POST',
                data: JSON.stringify(newEvent),
                success: function (response, textStatus, jqXhr) {
                    console.log("Hooray we created a new Event!!");
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    // log the error to the console
                    console.log("The following error occured: " + textStatus, errorThrown);
                },
                complete: function () {
                    console.log("event ajax completed");
                }
            });
        },

        createNewVenue = function (name, address) {
            $.get(baseUrl + ':8080/wutup/geocode?address=' + parsedForUrl(address), function (data) {
                var newVenue = {
                    name: name,
                    address: address,
                    latitude: data.latitude,
                    longitude: data.longitude
                };
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    url: baseUrl + ':8080/wutup/venues/',
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(newVenue),
                    success: function (response, textStatus, jqXhr) {
                        console.log("Hooray We Added A New Venue!");
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log("The following error occured: " + textStatus, errorThrown);

                    },
                    complete: function () {
                        console.log("venue ajax completed");
                    }
                });
            });
        },

        createOccurrence = function (eventName, venueName, start, end) {
            console.log(eventName, venueName);
            $.get(baseUrl + ':8080/wutup/events?name=' + parsedForUrl(eventName), function (events) {
                $.get(baseUrl + ':8080/wutup/venues?name=' + parsedForUrl(venueName), function (venues) {
                    var newOccurrence = {
                        event: events[0],
                        venue: venues[0],
                        start: start,
                        end: end
                    };
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        url: baseUrl + ':8080/wutup/occurrences/',
                        type: 'POST',
                        dataType: 'json',
                        data: JSON.stringify(newOccurrence),
                        success: function (response, textStatus, jqXHR) {
                            console.log("Hooray We Added A New Occurrence!!");
                            $.extend(newOccurrence, {id: response});
                            mapMarkers.push(createMarker(newOccurrence))
                            renderEventOnCalendar(newOccurrence);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.log("The following error occured: " + textStatus, errorThrown);
                            showError("Occurrence not created.");

                        },
                        complete: function (jqXHR, textStatus) {
                            console.log(jqXHR.getAllResponseHeaders());
                        }
                    });
                });
            });

        },

        patchEvent = function (event, eventDescription, eventCreator) {
            var eventPatch = {
                description: eventDescription
            }
            $.ajax({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                url: baseUrl + ':8080/wutup/events/' + event.id,
                type: 'PATCH',
                data: JSON.stringify(eventPatch),
                xhr: function () {
                    return window.XMLHttpRequest == null || new window.XMLHttpRequest().addEventListener == null ? new window.ActiveXObject("Microsoft.XMLHTTP") : $.ajaxSettings.xhr();
                },
                success: function (response, textStatus, jqXhr) {
                    console.log("Event Successfully Patched!");
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    // log the error to the console
                    console.log("The following error occured: " + textStatus, errorThrown);
                },
                complete: function () {
                    console.log("Event Patch Ran");
                }
            });
        },

        patchVenue = function (venue, address) {
            $.get(baseUrl + ':8080/wutup/geocode?address=' + parsedForUrl(address), function (data) {
                var venuePatch = {
                    address: address,
                    latitude: data.latitude,
                    longitude: data.longitude
                };
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    url: baseUrl + ':8080/wutup/venues/' + venue.id,
                    type: 'PATCH',
                    data: JSON.stringify(venuePatch),
                    xhr: function () {
                        return window.XMLHttpRequest == null || new window.XMLHttpRequest().addEventListener == null ? new window.ActiveXObject("Microsoft.XMLHTTP") : $.ajaxSettings.xhr();
                    },
                    success: function (response, textStatus, jqXhr) {
                        console.log("Venue Successfully Patched!");
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        // log the error to the console
                        console.log("The following error occured: " + textStatus, errorThrown);
                    },
                    complete: function () {
                        console.log("Venue Patch Ran");
                    }
                });
            });
        },

        patchOccurrence = function (occurrence) {
        	var newOccurrence = {
        		event: occurrence.event,
        		venue: occurrence.venue,
        		start: occurrence.start,
        		end: occurrence.end
        	}
            $.ajax({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                url: baseUrl + ':8080/wutup/occurrences/' + occurrence.id,
                type: 'PATCH',
                data: JSON.stringify(newOccurrence),
                xhr: function () {
                    return window.XMLHttpRequest == null || new window.XMLHttpRequest().addEventListener == null ? new window.ActiveXObject("Microsoft.XMLHTTP") : $.ajaxSettings.xhr();
                },
                success: function (response, textStatus, jqXhr) {
                    console.log("Occurrence Successfully Patched!");
                    removeEventOnCalendar(occurrence);
                    renderEventOnCalendar(occurrence);
                    removeMarker(occurrence.event.name, mapMarkers);
                    createMarker(occurrence);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    // log the error to the console
                    console.log("The following error occured: " + textStatus, errorThrown);
                },
                complete: function () {
                    console.log("Occurrence Patch Ran");
                }
            });
        },


        deleteOccurrence = function (occurrence) {
            $.ajax({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                url: baseUrl + ':8080/wutup/occurrences/' + occurrence.id,
                type: 'DELETE',
                xhr: function () {
                    return window.XMLHttpRequest == null || new window.XMLHttpRequest().addEventListener == null ? new window.ActiveXObject("Microsoft.XMLHTTP") : $.ajaxSettings.xhr();
                },
                success: function (response, textStatus, jqXhr) {
                    console.log("Occurrence Successfully Deleted!");
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    // log the error to the console
                    console.log("The following error occured: " + textStatus, errorThrown);
                },
                complete: function () {
                    console.log("Occurrence Deletion Ran");
                }
            });
        },

        createOrPatchEvent = function (name, description, creator) {
            console.log("Finding or Creating an Event");
            $.get(baseUrl + ':8080/wutup/events?name=' + parsedForUrl(name), function (data) {
                if (data.length > 0) {
                    console.log("Event Found!");
                    patchEvent(data[0], description, creator);
                } else {
                    console.log("Creating an Event");
                    createNewEvent(name, description, creator);
                }
            });
        },

        createOrPatchVenue = function (venueName, venueAddress) {
            console.log("Finding or Creating Venue");
            $.get(baseUrl + ':8080/wutup/venues?name=' + parsedForUrl(venueName), function (data) {
                if (data.length > 0) {
                    console.log("Venue Found!");
                    patchVenue(data[0], venueAddress);
                } else {
                    console.log("Creating a new Venue");
                    createNewVenue(venueName, venueAddress);
                }
            });
        },

        renderEventOnCalendar = function (occurrence) {
            $('#calendar').fullCalendar('renderEvent', {
                id: occurrence.id,
                title: occurrence.event.name,
                description: occurrence.event.description,
                start: occurrence.start,
                end: occurrence.end,
                allDay: false,
                venue: occurrence.venue,
                event: occurrence.event
            }, true // make the event "stick"
            );
        },

        removeEventOnCalendar = function (occurrence) {
            $('#calendar').fullCalendar('removeEvents', occurrence.id);
        },

        createCalendar = function (calendarEvents) {

            var clearOrPopulateDialogFields = function (calEvent) {
                $('#event-dialog').removeClass('hidden');
                $('#event-dialog').removeAttr("title");
                if (calEvent === null) {
                    $('#event-name').val("");
                    $('#event-description').val("");
                    $('#venue-name').val("");
                    $('#venue-address').val("");
                } else {
                    $("#event-name").val(calEvent.title);
                    $("#event-description").val(calEvent.event.description);
                    $('#venue-name').val(calEvent.venue.name);
                    $("#venue-address").val(calEvent.venue.address);
                }
            },

            addDeleteButtonToDialog = function (occurrence) {
                var buttons = $('#event-dialog').dialog('option', 'buttons')
                $.extend(buttons, {
                    "Delete Event": function () {
                        $(this).dialog("close");
                        deleteOccurrence(occurrence);
                        removeEventOnCalendar(occurrence);
                    }
                });
                $('#event-dialog').dialog('option', 'buttons', buttons);
            },

            showEventDialog = function (calEvent, start, end) {
                clearOrPopulateDialogFields(calEvent);
                $('#event-dialog').dialog({
                    show: "fade",
                    hide: "explode",
                    modal: true,
                    title: calEvent === null ? "Create Event" : "Edit Event",
                    position: {
                        my: 'top',
                        at: 'top',
                        of: $("#calendar")
                    },
                    buttons: {
                        "Save Event!": function () {
                            $(this).dialog("close");
                            var eventName = $('#event-name').val(),
                                venueName = $('#venue-name').val(),
                                creator = user;
                            createOrPatchEvent(eventName, $('#event-description').val(), creator);
                            createOrPatchVenue(venueName, $('#venue-address').val());
                            setTimeout(function () {
                                if (calEvent === null) {
                                    createOccurrence(eventName, venueName, start, end);
                                } else {
                                    $.getJSON(baseUrl + ':8080/wutup/events?name=' + parsedForUrl(eventName), function (events) {
                                    	$.getJSON(baseUrl + ':8080/wutup/venues?name=' + parsedForUrl(venueName), function (venues) {
                                    		calEvent.event = events[0];
                                    		calEvent.venue = venues[0];
                                    		patchOccurrence(calEvent);
                                    	});
                                	});
                                }
                            }, 3000);
                            calendar.fullCalendar('unselect');
                        },
                        Cancel: function () {
                            $(this).dialog("close");
                        }
                    }
                });
                if (calEvent != null) {
                    addDeleteButtonToDialog(calEvent);

                }
            },

            calendar = $('#calendar').fullCalendar({
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                theme: true,
                selectable: true,
                selectHelper: true,
                select: function (start, end, allDay) {
                    if (!allDay) {
                        showEventDialog(null, start, end);
                    }
                },
                dayClick: function (date, allDay, jsEvent, view) {
                    if (allDay) {
                        calendar.fullCalendar('gotoDate', date.getFullYear(), date.getMonth(), date.getDate());
                        calendar.fullCalendar('changeView', 'agendaDay');
                    }
                },
                eventClick: function (calEvent, jsEvent, view) {
                    showEventDialog(calEvent, calEvent.start, calEvent.end);
                },
                eventRender: function (event, element) {},
                editable: true,
                eventDrop: function (occurrence, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view) {
                    patchOccurrence(occurrence);
                },
                eventResize: function (occurrence, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view) {
                    patchOccurrence(occurrence);
                },
                events: calendarEvents
            });
        };

    initializeManageEventsPage();

};