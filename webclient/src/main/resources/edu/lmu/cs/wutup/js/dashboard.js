$(document).ready(function() {
	var map, infowindow;
	var instantiateMapAndCalendar = function() {
		var initialLocation, siberia = new google.maps.LatLng(60, 105), newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687), browserSupportFlag = new Boolean(), calendarEvents = null, mapOptions = {
			zoom : 10,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
		createCalendar(calendarEvents);
		// Trying W3C Geolocation
		if (navigator.geolocation) {
			browserSupportFlag = true;
			navigator.geolocation.getCurrentPosition(function(position) {
				initialLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
				map.setCenter(initialLocation);
				var bounds, radius, center;
				google.maps.event.addListener(map, 'tilesloaded', function() {
					bounds = map.getBounds();
					radius = bounds.toSpan().lng();
					center = bounds.getCenter();
					console.log(center.toString());
					// Grab Event Occurrences. Needs to be refined to map area.
					$.getJSON('http://localhost:8080/wutup/occurrences?page=0&pageSize=20', function(occurrences) {//&center=' + center.lat() + "," + center.lng() + '&radius=' + radius
						calendarEvents = parseOccurrencesForCalendar(occurrences);
						populateCalendar(calendarEvents);
						populateMap(map, calendarEvents);
					});

				});

			}, function() {
				handleNoGeolocation(browserSupportFlag);
			});

			// Browser doesn't support Geolocation
		} else {
			browserSupportFlag = false;
			handleNoGeolocation(browserSupportFlag);
		}

		var handleNoGeolocation = function(errorFlag) {
			if (errorFlag == true) {
				alert("Geolocation service failed.");
				initialLocation = newyork;
			} else {
				alert("Your browser doesn't support geolocation. We've placed you in Siberia.");
				initialLocation = siberia;
				infowindow = new google.maps.InfoWindow({
					map : map,
					position : initialLocation,
					content : 'Here\'s what\'s going on in Siberia...'
				});
			}
			map.setCenter(initialLocation);
		};
	};

	var populateMap = function(gMap, events) {
		map = gMap;
		for (var i = 0; i < events.length; i++) {
			createMarker(events[i]);
		}
	}
	var createMarker = function(occurrence) {
		var marker = new google.maps.Marker({
			map : map,
			position : new google.maps.LatLng(occurrence.venue.latitude, occurrence.venue.longitude),
			title : occurrence.event.name,
			animation : google.maps.Animation.DROP
		});
		google.maps.event.addListener(marker, 'click', function() {
			displayInfoWindow(occurrence);
			displayEventInfo(occurrence);
		});
		return marker;
	}
	var displayInfoWindow = function(occurrence) {
		if (infowindow)
			infowindow.close();
		infowindow = new google.maps.InfoWindow({
			content : occurrence.event.name + '</br>',
			position : new google.maps.LatLng(occurrence.venue.latitude, occurrence.venue.longitude)
		});
		infowindow.open(map);
	};

	var displayEventInfo = function(occurrence) {
		$("#result").html(function() {
			return "<h3>" + occurrence.event.name + "</h3>" + "<p>" + occurrence.event.description + "</p>" + "<h4>" + occurrence.venue.name + "</h4>" + "<p><b>Address:</b> " + occurrence.venue.address + "</p>" + "<p><b>Start:</b> " + occurrence.start + "</p>" + "<p><b>End:</b> " + occurrence.end + "</p>";
		});

	};

	var parseOccurrencesForCalendar = function(occurrences) {
		var calendarEvents = [];
		for (var i = 0; i < occurrences.length; i++) {
			calendarEvents.push({
				title : occurrences[i].event.name,
				start : new Date(occurrences[i].start),
				end : new Date(occurrences[i].end),
				event : occurrences[i].event,
				venue : occurrences[i].venue,
				allDay : false

			});
		}
		return calendarEvents;
	};

	var createCalendar = function(calendarEvents) {
		var calendar = $('#calendar').fullCalendar({
			header : {
				left : 'prev,next today',
				center : 'title',
				right : 'month,agendaWeek,agendaDay'
			},
			theme : true,
			editable : false,
			disableDragging : true,
			dayClick : function(date, allDay, jsEvent, view) {
				if (allDay) {
					calendar.fullCalendar('gotoDate', date.getFullYear(), date.getMonth(), date.getDate());
					calendar.fullCalendar('changeView', 'agendaDay');
				}
			},
			eventClick : function(event, jsEvent, view) {//'event' is used here instead of 'occurrence' due to fullcalendar.js documentation
				displayInfoWindow(event);
				displayEventInfo(event);
			},
			events : calendarEvents
		});
	};

	var populateCalendar = function(calendarEvents) {
		$('#calendar').fullCalendar('removeEventSource', calendarEvents);
		$('#calendar').fullCalendar('removeEvents');
		$('#calendar').fullCalendar('addEventSource', calendarEvents);
	};
	instantiateMapAndCalendar();
	//Start your Engines!

});
