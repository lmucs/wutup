window.fbAsyncInit = function() {
    FB.init({
      appId      : '525740714109980', // App ID
      channelUrl : '//localhost:9090/wutup/channel.html', // Channel File
      status     : true, // check login status
      cookie     : true, // enable cookies to allow the server to access the session
      xfbml      : true  // parse XFBML
    });

    var baseUrl = window.location.protocol + "//" + window.location.hostname;
    
    // Additional init code here
    FB.getLoginStatus(function(response) {
    	  if (response.status === 'connected') {
    	    // connected
    		  FB.api('/me', function(response) {
    			  $.getJSON(baseUrl + ':8080/wutup/users?fbId=' + response.id, function (user) {
    				  loadPageFunctionality(baseUrl, user);
    			  });
    			});
    	  } else {
    	    // not_logged_in
    		  loadPageFunctionality(baseUrl, null);
    	  }
    	 });
  };

  // Load the SDK Asynchronously
  (function(d){
     var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement('script'); js.id = id; js.async = true;
     js.src = "//connect.facebook.net/en_US/all.js";
     ref.parentNode.insertBefore(js, ref);
   }(document));