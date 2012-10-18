package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

public class EventResourceIT {

    @Test
    public void endpointGetFindsExistingEventJson() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            // 200 = request fulfilled, returning 404 in error
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"name\":\"Poker Night\"")).
            body(containsString("\"description\":\"Cards with the guys\"")).
            body(containsString("Katrina")).
        when().
            get("/wutup/events/1");
    }

    @Test
    public void endpointGetFindsExistingEventXML() {
        expect().
            statusCode(200).
            // 200 = request fulfilled
            contentType("application/xml").
            body(containsString("<id>1</id>")).
            body(containsString("<event>")).
        when().
            get("/wutup/events/1");
    }

    @Test
    public void endpointGetWithUnusedIdProduces404() {
        expect().
            statusCode(404).
            // 404 = not found
            body(containsString("Event 100")).
        when().
            get("/wutup/events/100");
    }

    @Test
    public void endpointPutWithUnusedIdProduces404() {
        given().
            header("Accept", "application/json").
            contentType("application/json").
            body("{\"id\":100,\"name\":\"Ski Trip\",\"description\":\"x\",\"comments\":null,\"ownerId\":{\"id\":8,\"email\":\"ksherbina@gmail.com\",\"nickname\":\"Kat\",\"fullName\":\"Katrina Sherbina\",\"firstname\":\"Katrina\",\"lastname\":\"Sherbina\"}}");
        expect().
            statusCode(404).
        when().
            put("/wutup/events/100");
    }

    @Test
    public void endpointPutWithMismatchedIdProduces409() {
        given().
            header("Accept", "application/json").
            contentType("application/json").
            body("{\"id\":\"172\",\"name\":\"Ski Trip\"}").
        expect().
            statusCode(409).
        when().
            put("/wutup/events/46");
    }

    @Test
    public void endpointPostJsonCorrectlyCreatesEventAndProduces201() {
        given().
            header("Accept", "application/json").
            contentType("application/json").
            body("{\"id\":\"9\",\"name\":\"Ski Trip\"}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/wutup/events/9").
            contentType("application/json").
        when().
            post("/wutup/events");
    }
}
