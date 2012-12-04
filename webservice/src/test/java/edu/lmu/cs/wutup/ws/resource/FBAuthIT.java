package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.containsString;

import org.junit.Ignore;
import org.junit.Test;

public class FBAuthIT {
    
    @Test
    public void fbAuthFailsWhenErrorIsPresent() {
        expect().
            statusCode(401).
        when().
            get("/wutup/auth/facebook?error=someerrorstring");
    }
    
    @Ignore
    @Test
    public void syncFacebookEventsWithFBCodeSucceeds() {
        expect().
            statusCode(200).
        when().
            get("/wutup/auth/facebook/sync?code=somecodestring12");
    }

    @Test
    public void fbSyncFailsWhenErrorIsPresent() {
        expect().
            statusCode(401).
        when().
            get("/wutup/auth/facebook/sync?error=someerrorstring");
    }

    @Ignore
    @Test
    public void getFacebookEventsWithFBCodeSucceeds() {
        expect().
            statusCode(200).
        when().
            get("/wutup/auth/facebook?code=somecodestring12");
    }

    @Test
    public void retrieveNonexistentUserByFacebookId() {
        expect().
            statusCode(200).
            body(containsString("{}")).
        when().
            get("/wutup/auth/someSessionId12");
    }
    
    @Test
    public void retrieveExistingUserByFacebookId() {
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
