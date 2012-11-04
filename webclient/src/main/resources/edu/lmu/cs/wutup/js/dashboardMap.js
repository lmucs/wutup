$(document).ready(function() {
  var initialLocation;
  var siberia = new google.maps.LatLng(60, 105);
  var newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687);
  var browserSupportFlag =  new Boolean();
  var myOptions = {
    zoom: 10,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

  // Try W3C Geolocation (Preferred)
  if(navigator.geolocation) {
    browserSupportFlag = true;
    navigator.geolocation.getCurrentPosition(function(position) {
      initialLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
      var infowindow = new google.maps.InfoWindow({
          map: map,
          position: initialLocation,
          content: 'You are here'
       });
      map.setCenter(initialLocation);
      $.get('http://localhost:8080/wutup/venues?page=0&pageSize=20', function(data){
    	  populateMap(map, data);
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
      alert("Your browser doesn't support geolocation. We've events[i]d you in Siberia.");
      initialLocation = siberia;
      var infowindow = new google.maps.InfoWindow({
          map: map,
          position: initialLocation,
          content: 'Here\'s what\'s going on in Siberia...'
       });
    }
    map.setCenter(initialLocation);
  }

  function populateMap(gMap, events) { //This would be the method that populates the map with events.
  	map = gMap;
  	for (var i = 0; i < events.length; i++) {
  		createMarker(events[i]);
  	}
  }
  
  function createMarker (event) {
      var infowindow = new google.maps.InfoWindow();
	  var marker = new google.maps.Marker( {
			map: map,
			position: new google.maps.LatLng(event.latitude,event.longitude),
			title: event.name,
			animation: google.maps.Animation.DROP
		});
		google.maps.event.addListener(marker, 'click', function() {
			infowindow.setContent(
					event.name + '</br>'
					);
			infowindow.open(map, this);
	    });
		return marker;
  }

});
