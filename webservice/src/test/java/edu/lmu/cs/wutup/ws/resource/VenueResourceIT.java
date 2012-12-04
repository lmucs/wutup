package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.joda.time.DateTime;
import org.junit.Test;

public class VenueResourceIT {
    
    /*
     * (1, 'Pantages Theater', '6233 Hollywood Bl, Los Angeles, CA', 34.1019444, -118.3261111);
     * (2, 'Hollywood Bowl', '2301 North Highland Ave, Hollywood, CA', 34.1127863, -118.3392439);
     * (3, 'Tochka', '8915 Sunset Bl, West Hollywood, CA', 34.090608, -118.386178);
     * (4, 'Griffith Observatory', '2800 East Observatory Rd, Los Angeles, CA 90027', 0, 0);
     * (5, 'The Roxy', '9009 West Sunset Bl, West Hollywood, CA 90069', 34.123408, -118.302409);
     * (6, 'The Viper Room', '8852 West Sunset Bl, West Hollywood, CA 90069', 34.090512, -118.384657);
     * (8, 'Carousel Restaurant', '304 N Brand Bl, Glendale, CA 91203', 34.149885, -118.255108);
     */
    
    @Test
    public void testGetVenueFindsEndpoint() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":2")).
            body(containsString("\"name\":\"Hollywood Bowl\"")).
            body(containsString("\"address\":\"2301 North Highland Ave, Hollywood, CA\"")).
            body(containsString("\"latitude\":34.1127863")).
            body(containsString("\"longitude\":-118.3392439")).
        when().
            get("/wutup/venues/2");
    }
    
    @Test
    public void testGetVenueByNameFindsEndpoint() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"name\":\"Pantages Theater\"")).
            body(containsString("\"address\":\"6233 Hollywood Bl, Los Angeles, CA\"")).
            body(containsString("\"latitude\":34.1019444")).
            body(containsString("\"longitude\":-118.3261111")).
        when().
            get("wutup/venues?name=Pan");
    }
    
    @Test
    public void testGetVenueByEventIdFindsEndpoint() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":4")).
            body(containsString("\"name\":\"Griffith Observatory\"")).
            body(containsString("\"address\":\"2800 East Observatory Rd, Los Angeles, CA 90027\"")).
            body(containsString("\"latitude\":0")).
            body(containsString("\"longitude\":0")).
        when().
            get("/wutup/venues?event=8");
    }
    
    @Test
    public void getVenueByPartialCircleSearchResponds400() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/venues?center=-100.0,20.0");
        
        given().
            header("Accept", "application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/venues?radius=20");
    }
    
    @Test
    public void patchToExistingVenueCanBeRetrieved() {
        given().
            contentType("application/json").
            body("{\"name\":\"The Echo\"}").
        expect().
            statusCode(204).
        when().
            patch("/wutup/venues/3");
        
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"name\":\"The Echo\"")).
            body(containsString("\"id\":3")).
            body(containsString("\"address\":\"8915 Sunset Bl, West Hollywood, CA\"")).
            body(containsString("\"latitude\":34.090608")).
            body(containsString("\"longitude\":-118.386178")).
        when().
            get("/wutup/venues/3");
    }

    @Test
    public void patchToVenueWithMismatchedIdsResponds409Conflict() {
        given().
            contentType("application/json").
            body("{\"id\":27,\"name\":\"The Echo\"}").
        expect().
            statusCode(409).
        when().
            patch("/wutup/venues/3");
    }

    @Test
    public void testFindVenuesbyIncompleteCircleSearchResponds400() {
        given().
            contentType("application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/venues?radius=3.14");
        
        given().
            contentType("application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/venues?center=-24.5,100.02");
        
        given().
            contentType("application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/venues?center=-22.0001&radius=50.0");
            
    }

    @Test
    public void testFindVenuesByCircleSearch() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"id\":1")).
            body(containsString("\"name\":\"Pantages Theater\"")).
            body(containsString("\"address\":\"6233 Hollywood Bl, Los Angeles, CA\"")).
            body(containsString("\"latitude\":34.1019444")).
            body(containsString("\"longitude\":-118.3261111")).
        when().
            get("/wutup/venues/?center=34.1019444,-118.3261111&radius=0.01");
    }
    
    @Test
    public void testPostToVenuesWithId() {
        given().
            contentType("application/json").
            body("{\"id\":26,\"name\":\"Test Venue\",\"address\":\"6555 Test St, Los Angeles, CA\",\"latitude\":34.1019444,\"longitude\":-188.3261111}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/wutup/venues/11").
        when().
            post("/wutup/venues");
    }

    @Test
    public void testPostToVenuesWithoutId() {
        given().
            contentType("application/json").
            body("{\"name\":\"Test Venue\",\"address\":\"6555 Test St, Los Angeles, CA\",\"latitude\":34.1019444,\"longitude\":-188.3261111}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/wutup/venues/12").
        when().
            post("/wutup/venues");
    }

    // **************************** PROPERTY TESTING ****************************
    @Test
    public void testGetVenuePropertiesById() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"Parking\":\"Valet only\"")).
            body(containsString("\"Ages\":\"18+\"")).
        when().
            get("/wutup/venues/2/properties");
    }

    @Test
    public void testGetVenuePropertiesWithNonIntegerIdResponsd400() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/venues/hoopla/properties");
    }

    @Test
    public void testGetVenuePropertiesWithoutIdResponds400() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(404).
        when().
            get("/wutup/venues//properties");
    }

    @Test
    public void testGetEmptyVenuePropertiesResponds200WithEmptyBody() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(equalTo("{}")).
        when().
            get("/wutup/venues/6/properties");
    }

    @Test
    public void testGetVenuePropertiesWithNonExistantVenueIdResponds404() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(404).
        when().
            get("/wutup/venues/8008135/properties");
    }

    @Test
    public void testUpdatePropertyWithNonIntegerIdResponds400() {
        given().
            contentType("application/json").
            body("{\"Parking\":\"NO PARKING\"}").
        expect().
            statusCode(400).
        when().
            post("/wutup/venues/hoopla/properties");
    }

    @Test
    public void testUpdatedPropertyCanBeFound() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"Parking\":\"Valet only\"")).
        when().
            get("/wutup/venues/2/properties");

        given().
            contentType("application/json").
            body("{\"Parking\":\"Free parking for all!\"}").
        expect().
            statusCode(204).
        when().
            post("/wutup/venues/2/properties");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"Parking\":\"Free parking for all!\"")).
        when().
            get("/wutup/venues/2/properties");
    }

    @Test
    public void testUpdateNonExistantPropertyResponds204() {
        given().
            contentType("application/json").
            body("{\"ID Required\":\"No\"}").
        expect().
            statusCode(204).
        when().
            post("/wutup/venues/7/properties");
    }

    @Test
    public void testUpdatePropertyOnNonExistantResponds404() {
        given().
            contentType("application/json").
            body("{\"Parking\":\"No\"}").
        expect().
            statusCode(404).
        when().
            post("/wutup/venues/8008135/properties");
    }

    @Test
    public void testUpdatePropertyOnlyTakesOneKeyValuePair() {
        given().
            contentType("application/json").
            body("{\"fax\":\"Use the damn phone\",\"twitter\":\"@turd\"}").
        expect().
            statusCode(400).
        when().
            post("/wutup/venues/5/properties");
    }

    @Test
    public void testUpdatePropertyWithEmptyBodyResponds400() {
        given().
            contentType("application/json").
            body("{}").
        expect().
            statusCode(400).
        when().
            post("/wutup/venues/5/properties");
    }
    
    @Test
    public void testDeletePropertyWithNullValue() {
        given().
            contentType("application/json").
            body("{\"fax\":null}").
        expect().
            statusCode(204).
        when().
            post("/wutup/venues/5/properties");
        
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("{\"twitter\":\"@theroxy\"")). // This needs to be more specific
        when().
            get("/wutup/venues/5/properties");
    }
    
    @Test
    public void testCreatingProperty() {
        given().
            contentType("application/json").
            body("{\"Food\":\"No\"}").
        expect().
            statusCode(204).
        when().
            post("/wutup/venues/5/properties");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"Food\":\"No\"")).
        when().
            get("/wutup/venues/5/properties");
    }
    // **************************** END PROPERTY TESTING ****************************

    // **************************** COMMENT TESTING ****************************
    
    @Test
    public void testfindVenueCommentCanBeRead() {
        DateTime postDateOne = new DateTime(2012, 3, 30, 12, 34, 56);
        DateTime postDateTwo = new DateTime(2012, 12, 25, 7, 0, 0);
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"id\":1")).
            body(containsString("\"email\":\"40mpg@gmail.com\"")).
            body(containsString("\"body\":\"This venue sux.\"")).
            body(containsString("\"postdate\":" + postDateOne.getMillis())).
            body(containsString("\"id\":2")).
            body(containsString("\"author\":{\"id\":2")).
            body(containsString("\"body\":\"My life is a sham\"")).
            body(containsString("\"postdate\":" + postDateTwo.getMillis())).
        when().
            get("wutup/venues/10/comments");
    }
    
    @Test
    public void testFindVenueCommentsOnVenueWithNoComments() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(equalTo("[]")).
        when().
            get("wutup/venues/2/comments");
    }
    
    @Test
    public void addedVenueCommentCanBeFound() {
        DateTime publishDate = new DateTime(2012, 11, 14, 13, 56, 21);
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(equalTo("[]")).
        when().
            get("/wutup/venues/4/comments");
        
        given().
            contentType("application/json").
            body("{\"author\":{\"id\":1,\"facebookId\":\"asd\",\"email\":\"40mpg@gmail.com\",\"nickname\":\"hybrid\",\"firstname\":\"Honda\",\"lastname\":\"Prius\"}," +
            		"\"body\":\"Hey everybody!\",\"postdate\":" + publishDate.getMillis() + "}").
		expect().
		    statusCode(204).
	    when().
	        post("wutup/venues/4/comments");
        
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"author\":{\"id\":1")).
            body(containsString("\"email\":\"40mpg@gmail.com\"")).
            body(containsString("\"postdate\":" + publishDate.getMillis())).
        when().
            get("/wutup/venues/4/comments");
        
    }
    
    @Test
    public void deleteVenueCommentCanNoLongerBeFound() {
        DateTime knownPublishDate = new DateTime(2012, 12, 25, 7, 0, 0);
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"author\":{\"id\":1")).
            body(containsString("\"email\":\"40mpg@gmail.com\"")).
            body(containsString("\"postdate\":" + knownPublishDate.getMillis())).
        when().
            get("/wutup/venues/6/comments");
        
        given().
            header("Accept", "application/json").
        expect().
            statusCode(204).
        when().
            delete("/wutup/venues/6/comments/3");
        
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(equalTo("[]")).
        when().
            get("/wutup/venues/6/comments");
    }
    
    @Test
    public void deleteNonExistantVenueCommentResponds404() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(404).
        when().
            delete("/wutup/venues/6/comments/5");
    }
    
    @Test
    public void addCommentToNonExistantVenueResponds404() {
        DateTime publishDate = new DateTime(2012, 11, 14, 13, 56, 21);
        given().
            contentType("application/json").
            body("{\"author\":{\"id\":1,\"email\":\"40mpg@gmail.com\",\"nickname\":\"hybrid\",\"firstname\":\"Honda\",\"lastname\":\"Prius\"}," +
                    "\"body\":\"Hey everybody!\",\"postdate\":" + publishDate.getMillis() + "}").
        expect().
            statusCode(404).
        when().
            post("wutup/venues/666/comments");
    }
    
    // ******************** End Comment Testing **********************
}
