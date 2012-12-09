$('a[href='+ document.URL.split('/').pop() + ']').parent().addClass('active').css('font-weight', 'bold');



var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-36871099-1']);
_gaq.push(['_setDomainName', 'cs.lmu.edu']);
_gaq.push(['_trackPageview']);

(function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();