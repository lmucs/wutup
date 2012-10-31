package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;

import org.junit.Ignore;
import org.junit.Test;

public class FBAuthIT {
    
    @Test
    public void redirectToFacebookForAuthentication() {
        expect().
            statusCode(200).
        when().
            get("/wutup/auth/facebook");
    }
    
    @Ignore
    @Test
    public void testSuccessfulAuthenticationLanding() {
        expect().
            statusCode(200).
        when().
            get("/wutup/auth/landing?state=637730135&code=AQA_5m_sNTv-g7HaAev6TYeNPb7naOU#_=_");
    }
    
    @Test
    public void testAuthenticationLandingWithErrorFromFacebook() {
        expect().
            statusCode(401).
        when().
            get("/wutup/auth/landing?error=someshitwentwrong#_=_");
    }
    
    @Ignore
    @Test
    public void testAuthenticationLandingWithAccessTokenFromFacebook() {
        expect().
            statusCode(200).
        when().
            get("/wutup/auth/landing?access_token=SomeSuper-Long_key&expires=346517");
    }
}
