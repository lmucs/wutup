$(document).ready(function() {
  var initialLocation;
  var siberia = new google.maps.LatLng(60, 105);
  var newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687);
  var browserSupportFlag =  new Boolean();
  var infowindow;
  var mapOptions = {
    zoom: 10,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  var map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
  
  var input = document.getElementById('event-address');
  var autocomplete = new google.maps.places.Autocomplete(input);
  autocomplete.bindTo('bounds', map);
  var infowindow = new google.maps.InfoWindow();

  var marker = new google.maps.Marker({
      map: map
    });
  google.maps.event.addListener(autocomplete, 'place_changed', function() {
  infowindow.close();
  marker.setVisible(false);
  input.className = '';
  var place = autocomplete.getPlace();
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
    map.setZoom(15);  // Why 17? Because it looks good.
  }
  var image = new google.maps.MarkerImage(
      place.icon,
      new google.maps.Size(71, 71),
      new google.maps.Point(0, 0),
      new google.maps.Point(17, 34),
      new google.maps.Size(35, 35));
  marker.setIcon(image);
  marker.setPosition(place.geometry.location);
  var address = '';
  if (place.address_components) {
    address = [
      (place.address_components[0] && place.address_components[0].short_name || ''),
      (place.address_components[1] && place.address_components[1].short_name || ''),
      (place.address_components[2] && place.address_components[2].short_name || '')
    ].join(' ');
  }
  infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
  infowindow.open(map, marker);
});

  
  // Trying W3C Geolocation 
  if(navigator.geolocation) {
    browserSupportFlag = true;
    navigator.geolocation.getCurrentPosition(function(position) {
      initialLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
      map.setCenter(initialLocation);
      // Grab Event Occurrences. Needs to be refined to map area. 
      $.get('http://localhost:8080/wutup/occurrences?page=0&pageSize=20', function(occurrences){
    	  console.log(occurrences);
    	  var calendarEvents = parseOccurrencesForCalendar(occurrences);
	      createCalendar(calendarEvents);
    	  populateMap(map, occurrences);
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
      var infowindow = new google.maps.InfoWindow({
          map: map,
          position: initialLocation,
          content: 'Here\'s what\'s going on in Siberia...'
       });
    }
    map.setCenter(initialLocation);
  }

  var populateMap = function(gMap, events) {
  	map = gMap;
  	for (var i = 0; i < events.length; i++) {
  		createMarker(events[i]);
  	}
  }
  
  var createMarker = function(occurrence) {
	  var marker = new google.maps.Marker( {
			map: map,
			position: new google.maps.LatLng(occurrence.venue.latitude,occurrence.venue.longitude),
			title: occurrence.event.name,
			animation: google.maps.Animation.DROP
		});
		google.maps.event.addListener(marker, 'click', function() {
			if (infowindow) infowindow.close();
			infowindow = new google.maps.InfoWindow({
					content: occurrence.event.name + '</br>'
			});
			infowindow.open(map, marker);
		    displayEventInfo(occurrence);
	    });
		return marker;
  }
  
  var displayEventInfo = function(occurrence) {
	  $("#result").html( function() {
			 return "<h3>" + occurrence.event.name + "</h3>" +
			  "<p>" + occurrence.event.description + "</p>";
	  });
	  
  }
  
  var parseOccurrencesForCalendar = function(occurrences) {
		var calendarEvents = [];
		for (var i = 0; i < occurrences.length; i++) {
			calendarEvents.push(
					    { title: occurrences[i].event.name,
						  start: new Date(occurrences[i].start),
						  end  : new Date(occurrences[i].end),
						  event: occurrences[i].event,
						  venue: occurrences[i].venue,
						  allDay: false
						  
						});
		}
		return calendarEvents;
	};
	var createCalendar = function(calendarEvents) {
		var calendar = $('#calendar').fullCalendar({
	        header: {
	            left: 'prev,next today',
	            center: 'title',
	            right: 'month,agendaWeek,agendaDay'
	        },
	        editable: true,
	        theme: true,
	        selectable: true,
	        selectHelper: true,
	        select: function(start, end, allDay) {
	            if (!allDay) {
	                $('#event-dialog').removeClass('hidden');
	                $('#event-name').val("");
	                $('#event-description').val("");
	                $('#event-address').val("");
	                $('#event-dialog').attr('title', 'Edit Event');
	                $('#event-dialog').dialog({
	                    show: "fade",
	                    hide: "explode",
	                    modal: true,
	                    position: { 
	                        my: 'top',
	                        at: 'top',
	                        of: $("#calendar")
	                    },
	                    buttons: {
	                        "Create Event!": function() {
	                            $(this).dialog("close");
	                            calendar.fullCalendar('renderEvent', {
	                                title: $("#event-name").val(),
	                                description: $("#event-description").val(),
	                                address: $("#event-address").val(), //This needs to be expanded into creating a new venue or associating with an existing one.
	                                start: start,
	                                end: end,
	                                allDay: false
	                            }, true // make the event "stick"
	                            );
	                            console.log(calendar.fullCalendar('clientEvents'));
	                            calendar.fullCalendar('unselect');
	                        },
	                        Cancel: function() {
	                            $(this).dialog("close");
	                        }
	                    }
	                });
	                start = start;
	                end = end;
	            }
	        },
	        dayClick: function(date, allDay, jsEvent, view) {
	            if (allDay) {
	                calendar.fullCalendar('gotoDate', date.getFullYear(), date.getMonth(), date.getDate());
	                calendar.fullCalendar('changeView', 'agendaDay');
	            }
	        },
	        eventClick: function(calEvent, jsEvent, view) {
	        	$('#event-dialog').removeClass('hidden');
	            $("#event-name").val(calEvent.title);
	            $("#event-description").val(calEvent.description);
	            $("#event-address").val(calEvent.venue.address);
	            $('#event-dialog').attr('title', 'Edit Event');
	            $('#event-dialog').dialog({
	                    show: "blind",
	                    hide: "explode",
	                    modal: true,
	                    position: { 
	                        my: 'top',
	                        at: 'top',
	                        of: $("#calendar")
	                    },
	                    buttons: {
	                        "Create Event!": function() {
	                            $(this).dialog("close");
	                            calendar.fullCalendar('renderEvent', {
	                                title: $("#event-name").val(),
	                                description: $("#event-description").val(),
	                                address: $("#event-address").val(),
	                                start: start,
	                                end: end,
	                                allDay: false
	                            }, true // make the event "stick"
	                            );
	                            console.log(calendar.fullCalendar('clientEvents'));
	                            calendar.fullCalendar('unselect');
	                        },
	                        Cancel: function() {
	                            $(this).dialog("close");
	                        }
	                    }
	                });
	        },
	        events: calendarEvents
	    });
	};

});
