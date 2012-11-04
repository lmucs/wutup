package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Ignore;
import org.junit.Test;

public class EventOccurrenceResourceIT {

    @Ignore
    @Test
    public void endpointGetFindsAllEventOccurrences() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"eventId\":\"8\"")).
            body(containsString("\"venue\":{\"id\":4")).
            body(containsString("\"start\":1352065446376")).
            body(containsString("\"end\":1352151846433")).
            body(containsString("\"comments\":null")).
        when().
            get("/wutup/occurrences/");
    }
}
