package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

public class GeocodeResourceIT {
    @Test
    public void redirectToFacebookForAuthentication() {
        expect().
            statusCode(200).
            body(containsString("\"latitude\":33.966605")).
            body(containsString("\"longitude\":-118.423562}")).
        when().
            get("/wutup/geocode?address=1+lmu+dr,+los+angeles,+ca");
    }
}
