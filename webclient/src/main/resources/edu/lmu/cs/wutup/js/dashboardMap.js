$(document).ready(function() {
  var initialLocation;
  var siberia = new google.maps.LatLng(60, 105);
  var newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687);
  var browserSupportFlag =  new Boolean();
  var myOptions = {
    zoom: 16,
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
          content: 'You are here... or atleast your browser says you are.'
       });
      map.setCenter(initialLocation);
      populateMap(map, initialLocation);
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

  function populateMap(gMap, loc) { //This would be the method that populates the map with events.
  	map = gMap;
	var request = {
	    location: loc,	//Center to search form
  	};

  	infowindow = new google.maps.InfoWindow();
  }

});
