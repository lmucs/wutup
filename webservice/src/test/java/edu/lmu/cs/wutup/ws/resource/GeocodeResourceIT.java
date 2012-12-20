package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.containsString;

import org.junit.Ignore;
import org.junit.Test;

public class GeocodeResourceIT {
    @Test
    @Ignore
    public void geocodeAddressToLatLong() {
        expect().
            statusCode(200).
            body(containsString("\"latitude\":33.966605")).
            body(containsString("\"longitude\":-118.423562}")).
        when().
            get("/wutup/geocode?address=1+lmu+dr,+los+angeles,+ca");
    }
    
    @Test
    public void noAddressRespondsWith400() {
        expect().
            statusCode(400).
        when().
            get("/wutup/geocode?address=");
    }
    
    @Test
    public void reverseGeocodeLatLongToAddress() {
        expect().
            statusCode(200).
            body(containsString("1 Loyola Marymount University Drive, " +
                    "Loyola Marymount University, Los Angeles, CA 90045, USA")).
        when().
            get("/wutup/geocode?lat=33.966605&lng=-118.423562");
    }
    
    @Test
    public void noLatRespondsWith400() {
        expect().
            statusCode(400).
        when().
            get("/wutup/geocode?lng=-118.423562");
    }
    
    @Test
    public void noLngRespondsWith400() {
        expect().
            statusCode(400).
        when().
            get("/wutup/geocode?lat=33.966605");
    }
    
    @Test
    public void emptyQueryParamsToGeocodeResourceReturns400() {
        expect().
            statusCode(400).
        when().
            get("/wutup/geocode");
    }
}
