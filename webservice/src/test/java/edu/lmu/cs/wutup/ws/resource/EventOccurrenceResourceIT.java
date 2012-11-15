package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;

public class EventOccurrenceResourceIT {

    @Test
    public void endpointGetFindsAllEventOccurrences() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"event\":{\"id\":8")).
            body(containsString("\"venue\":{\"id\":4")).
            body(containsString("\"comments\":null")).
        when().
            get("/wutup/occurrences/");
    }

    @Test
    public void endpointGetWithEventIdQueryFindsExistingEventOccurrences() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("[{\"id\":1,\"event\":{\"id\":2")).
            body(containsString("{\"id\":6,\"event\":{\"id\":2")).
        when().
            get("/wutup/occurrences?eventId=2");
    }

    @Test
    @Ignore
    public void endpointGetWithTimeIntervalQueryFindsExistingEventOccurrences() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("[{\"id\":1,\"event\":{\"id\":2")).
        when().
            get("/wutup/occurrences?start=1326369600&end=1327060800");
    }

    @Test
    public void endpointGetHasJsonAsDefaultAcceptHeader() {
        expect().
            statusCode(200).
            contentType("application/json")
            .body(containsString("\"id\":3"))
            .body(containsString("\"event\":{\"id\":5"))
            .body(containsString("\"venue\":{\"id\":3"))
            .body(containsString("\"comments\":null"))
        .when()
            .get("/wutup/occurrences/3");
    }

    @Test
    public void endpointGetWithUnusedIdProduces404() {
        expect().statusCode(404).when().get("/wutup/Occurrences/100");
    }

    @Test
    public void endpointPostJsonWithoutIdCorrectlyCreatesEventOccurrenceAndProduces201() {
        DateTime sampleStartDate = new DateTime(2012, 12, 21, 12, 30);
        DateTime sampleEndDate = new DateTime(2012, 12, 21, 16, 35);

        given().contentType("application/json")
            .body("{\"event\":{\"id\":1},\"venue\":{\"id\":1},\"start\":" + sampleStartDate.getMillis()
                    + ",\"end\":" + sampleEndDate.getMillis() + "}")
        .expect()
            .statusCode(201)
            .header("Location", "http://localhost:8080/wutup/occurrences/11")
            .contentType("application/json")
        .when()
            .post("/wutup/occurrences");
    }
}
