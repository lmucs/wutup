package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.containsString;

import org.junit.Ignore;
import org.junit.Test;

public class FBAuthIT {
    
    @Ignore
    @Test
    public void getFacebookEvents() {
        expect().
            statusCode(200).
        when().
            get("/wutup/auth/facebook/events");
    }
    
    @Ignore
    @Test
    public void getFacebookEventsWithErrorFails() {
        expect().
            statusCode(401).
        when().
            get("/wutup/auth/facebook/events?error=someErrorThatFacebookSent");
    }
    
    @Test
    public void retrieveNonexistentUserBySessionId() {
        expect().
            statusCode(200).
            body(containsString("{}")).
        when().
            get("/wutup/auth/someSessionId12");
    }
    
    @Test
    public void retrieveExistingUserBySessionId() {
        expect().
            statusCode(200).
            body(containsString("40mpg@gmail.com")).
            body(containsString("hybrid")).
            body(containsString("Honda")).
            body(containsString("Prius")).
        when().
            get("/wutup/auth/hybrid");
    }
}
