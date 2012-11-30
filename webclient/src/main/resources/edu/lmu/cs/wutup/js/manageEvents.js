$(document).ready(function () {
	"use strict";
    var baseUrl = window.location.protocol + "//" + window.location.hostname,
        initializeManageEventsPage = function () {
            var initialLocation,
            	siberia = new google.maps.LatLng(60, 105),
                newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687),
                browserSupportFlag,
                mapOptions = {
                    zoom: 10,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                },
                map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions),
                input = document.getElementById('venue-address'),
                autocomplete = new google.maps.places.Autocomplete(input),
                infowindow = new google.maps.InfoWindow(),
                marker = new google.maps.Marker({
                    map: map
                });
            autocomplete.bindTo('bounds', map);
            google.maps.event.addListener(autocomplete, 'place_changed', function () {
                infowindow.close();
                marker.setVisible(false);
                input.className = '';
                var place = autocomplete.getPlace(), address = '';
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
                    // Grab Event Occurrences. Needs to be refined to map area.
                    $.getJSON(generateOccurrenceURL(), function (occurrences) {
                        console.log(occurrences);
                        var calendarEvents = parseOccurrencesForCalendar(occurrences);
                        createCalendar(calendarEvents);
                        populateMap(map, occurrences);
                    });

                }, function () {
                    handleNoGeolocation(browserSupportFlag);
                });

                // Browser doesn't support Geolocation
            } else {
                browserSupportFlag = false;
                handleNoGeolocation(browserSupportFlag);
            }
        },

        handleNoGeolocation = function (errorFlag) {
            if (errorFlag === true) {
                alert("Geolocation service failed.");
                initialLocation = newyork;
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

        generateOccurrenceURL = function () {
            return baseUrl + ':8080/wutup/occurrences?page=0&pageSize=50';
        },

        populateMap = function (gMap, events) {
            var map = gMap, i;
            for (i = 0; i < events.length; i += 1) {
                createMarker(map, events[i]);
            }
        },
        createMarker = function (map, occurrence) {
            var marker = new google.maps.Marker({
                map: map,
                position: new google.maps.LatLng(occurrence.venue.latitude, occurrence.venue.longitude),
                title: occurrence.event.name,
                animation: google.maps.Animation.DROP
            });
            google.maps.event.addListener(marker, 'click', function () {
                if (infowindow) infowindow.close();
                infowindow = new google.maps.InfoWindow({
                    content: occurrence.event.name + '</br>'
                });
                infowindow.open(map, marker);
                displayEventInfo(occurrence);
            });
            return marker;
        },
        displayEventInfo = function (occurrence) {
            $("#result").html(function () {
                return "<h3>" + occurrence.event.name + "</h3>" + "<p>" + occurrence.event.description + "</p>" + "<h4>" + occurrence.venue.name + "</h4>" + "<p><b>Address:</b> " + occurrence.venue.address + "</p>" + "<p><b>Start:</b> " + occurrence.start + "</p>" + "<p><b>End:</b> " + occurrence.end + "</p>";
            });

        },

        parseOccurrencesForCalendar = function (occurrences) {
            var calendarEvents = [], i;
            for (i = 0; i < occurrences.length; i += 1) {
                calendarEvents.push({
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

        createEventOccurrence = function (eventName, venueName, start, end) {
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
                        success: function (response, textStatus, jqXhr) {
                            console.log("Hooray We Added A New Occurrence!!");
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.log("The following error occured: " + textStatus, errorThrown);

                        },
                        complete: function () {
                            console.log("occurrence ajax completed");
                            renderEventOnCalendar(newOccurrence);
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

        patchVenue = function (venue, description) {
            var venuePatch = {
                description: description
            };
            $.ajax({
                headers: {
                    'Accept': 'application/json',
                        'Content-Type': 'application/json'
                },
                url: baseUrl + ':8080/wutup/venues/' + venue.id,
                type: 'PATCH',
                data: JSON.stringify(venuePatch),
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
        },

        patchOccurrence = function (id, occurrence) {
            $.ajax({
                headers: {
                    'Accept': 'application/json',
                        'Content-Type': 'application/json'
                },
                url: baseUrl + ':8080/wutup/occurrences/' + id,
                type: 'PATCH',
                data: JSON.stringify(occurrence),
                success: function (response, textStatus, jqXhr) {
                    console.log("Occurrence Successfully Patched!");
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

        findOrPatchEvent = function (name, description, creator) {
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

        findOrPatchVenue = function (venueName, venueAddress) {
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

        findOrPatchOccurrence = function (occurrence) {
            console.log("Finding or Creating Occurrence");
            $.get(baseUrl + ':8080/wutup/occurrences?event=' + parsedForUrl(occurrence.event.id), function (data) {
                for (i = 0; i < data.length; i += 1) {
                    if (data.venue.id === occurrence.venue.id && data.start === occurrence.start) {
                        console.log("Occurrence Found!");
                        patchOccurrence(data[0].id, occurrence);
                        return;
                    }
                }
                console.log("Creating a new Occurrence");
                createNewVenue(venueName, venueAddress);
            });
        },

        renderEventOnCalendar = function (occurrence) {
            $('#calendar').fullCalendar('renderEvent', {
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
                                    creator = {
                                        id: 155,
                                        firstname: "Carlos",
                                        lastname: "Agudo",
                                        nickname: "Big Sexy",
                                        email: "cagudo@gmail.com"
                                    };
                                findOrPatchEvent(eventName, $('#event-description').val(), creator);
                                findOrPatchVenue(venueName, $('#venue-address').val());
                                setTimeout(function () {
                                    createEventOccurrence(eventName, venueName, start, end);
                                }, 2000);
                                calendar.fullCalendar('unselect');
                            },
                            Cancel: function () {
                                $(this).dialog("close");
                            }
                        }
                    });
                },
                
	            calendar = $('#calendar').fullCalendar({
	                header: {
	                    left: 'prev,next today',
	                    center: 'title',
	                    right: 'month,agendaWeek,agendaDay'
	                },
	                editable: true,
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
	                    console.log(calEvent);
	                    showEventDialog(calEvent, calEvent.start, calEvent.end);
	                },
	                eventRender: function (event, element) {
	                    //                createMarker(event);
	                },
	                events: calendarEvents
	            });
        };

    initializeManageEventsPage();

});