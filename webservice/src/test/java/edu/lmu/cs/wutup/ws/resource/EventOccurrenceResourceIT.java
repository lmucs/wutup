package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;

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

    @Test
    public void endpointGetHasJsonAsDefaultAcceptHeader() {
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":3")).
            body(containsString("\"eventId\":5")).
            body(containsString("\"venue\":{\"id\":3")).
            body(containsString("\"comments\":null")).
        when().
            get("/wutup/occurrences/3");
    }

    @Test
    public void endpointGetWithUnusedIdProduces404() {
        expect().
            statusCode(404).
        when().
            get("/wutup/Occurrences/100");
    }

    @Test
    @Ignore
    public void endpointPostJsonCorrectlyCreatesEventOccurrenceAndProduces201() {
        given().
            contentType("application/json").
            body("{\"eventId\":4,\"venue\":{\"id\":1}}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/wutup/occurrences/6").
            contentType("application/json").
        when().
            post("/wutup/occurrences");
    }
}
