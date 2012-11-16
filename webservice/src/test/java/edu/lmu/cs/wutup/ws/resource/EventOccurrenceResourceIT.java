package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.joda.time.DateTime;
import org.junit.Test;

public class EventOccurrenceResourceIT {

    @Test
    public void getOccurrencesReturnsAllEventOccurrences() {
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
    public void getWithEventIdQueryFindsEventOccurrences() {
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
    public void getWithTimeIntervalQueryFindsEventOccurrences() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("[{\"id\":1,\"event\":{\"id\":2")).
        when().
            get("/wutup/occurrences?start=1325764800000&end=1338465600000");
    }

    @Test
    public void getWithCircleQueryFindsEventOccurrences() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"event\":{\"id\":6")).
            body(containsString("\"venue\":{\"id\":2")).
            body(containsString("\"name\":\"Hollywood Bowl\"")).
            body(containsString("\"address\":\"2301 North Highland Ave, Hollywood, CA\"")).
            body(containsString("\"latitude\":34.1127863")).
            body(containsString("\"longitude\":-118.3392439")).
        when().
            get("/wutup/occurrences/?center=34.1127863,-118.3392439&radius=0.01");
    }

    @Test
    public void getHasJsonAsDefaultAcceptHeader() {
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
    public void getWithUnusedIdProduces404() {
        expect().statusCode(404).when().get("/wutup/Occurrences/100");
    }

    @Test
    public void patchToEventOccurrencesWithMismatchedIdsResponds409() {
        given().
            contentType("application/json").
            body("{\"id\":27,\"start\":1325764800000}").
        expect().
            statusCode(409).
        when().
            patch("/wutup/occurrences/3");
    }

    @Test
    public void patchToExistingEventOccurrenceCanBeRetrieved() {

        given().
            contentType("application/json").
            body("{\"event\":{\"id\":1}}").
        expect().
            statusCode(204).
        when().
            patch("/wutup/occurrences/1");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"event\":{\"id\":1")).
        when().
            get("/wutup/occurrences/1");
    }

    @Test
    public void postJsonWithoutIdCorrectlyCreatesEventOccurrenceAndProduces201() {
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
