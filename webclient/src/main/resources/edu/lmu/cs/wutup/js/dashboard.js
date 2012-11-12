$(document).ready(function() {
  var initialLocation;
  var siberia = new google.maps.LatLng(60, 105);
  var newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687);
  var browserSupportFlag =  new Boolean();
  var infowindow;
  var myOptions = {
    zoom: 10,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

  // Trying W3C Geolocation 
  if(navigator.geolocation) {
    browserSupportFlag = true;
    navigator.geolocation.getCurrentPosition(function(position) {
      initialLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
      map.setCenter(initialLocation);
      // Grab Event Occurrences. Needs to be refined to map area. 
      $.get('http://localhost:8080/wutup/occurrences?page=0&pageSize=20', function(occurrences){
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

  function handleNoGeolocation(errorFlag) {
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

  function populateMap(gMap, events) {
  	map = gMap;
  	for (var i = 0; i < events.length; i++) {
  		createMarker(events[i]);
  	}
  }
  
  function createMarker (occurrence) {
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
		    $("#result").html(occurrence.event.name);
	    });
		return marker;
  }
  
  var parseOccurrencesForCalendar = function(occurrences) {
		var calendarEvents = [];
		for (var i = 0; i < occurrences.length; i++) {
			calendarEvents.push(
					    { title: occurrences[i].event.name,
						  start: new Date(occurrences[i].start),
						  end  : new Date(occurrences[i].end),
						  allDay: false
						  
						});
		}
		return calendarEvents;
	};
	var createCalendar = function(calendarEvents) {
		var calendar = $('.calendar').fullCalendar({
			header: {
				left: 'prev,next today',
				center: '',
				right: 'month,agendaWeek,agendaDay'
			},
			theme:true,
			editable: false,
          disableDragging: true,
		    dayClick: function(date, allDay, jsEvent, view) {
	            if (allDay) {
	                calendar.fullCalendar('gotoDate', date.getFullYear(), date.getMonth(), date.getDate());
	                calendar.fullCalendar('changeView', 'agendaDay');
	            }
		    },
			events: calendarEvents
		});
	};

});
