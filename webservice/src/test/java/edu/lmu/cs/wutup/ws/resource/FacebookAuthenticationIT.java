package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;

import org.junit.Test;

public class FacebookAuthenticationIT {
    @Test
    public void redirectToFacebookForAuthentication() {
        expect().
            statusCode(200).
        when().
            get("/wutup/auth/facebook");
    }
}
