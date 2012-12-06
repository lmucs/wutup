package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;

public class EventOccurrenceResourceIT {
    @Test
    public void getWithIdWorksAndHasJsonAsDefaultAcceptHeader() {
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
    public void getOccurrencesWithoutQueryReturns400() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/occurrences/");
    }

    @Test
    public void getWithPaginationFindsEventOccurrences() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("[{\"id\":1,\"event\":{\"id\":2")).
            body(not(containsString("{\"id\":6,\"event\":{\"id\":2"))).
        when().
            get("/wutup/occurrences?eventId=2&page=0&pageSize=1");
    }

    @Test
    public void getWithInvalidPaginationReturns403() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(403).
        when().
            get("/wutup/occurrences?eventId=2&page=1&pageSize=0");
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
            get("/wutup/occurrences?center=34.1127863,-118.3392439&radius=0.01");
    }

    @Test
    public void getWithInvalidCircleReturns400() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/occurrences?eventId=2&center=34.1127863,-118.3392439&radius=a");
    }

    @Test
    public void getWithOneEventIdQueryFindsEventOccurrences() {
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
    public void getWithMultipleEventIdQueryFindsEventOccurrences() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("{\"id\":1,\"event\":{\"id\":2")).
            body(containsString("{\"id\":6,\"event\":{\"id\":2")).
            body(containsString("{\"id\":5,\"event\":{\"id\":3")).
            body(containsString("{\"id\":10,\"event\":{\"id\":3")).
        when().
            get("/wutup/occurrences?eventId=2,3,4");
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
    public void getWithInvalidEventIdReturns400() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/occurrences?eventId=abba&start=1325764800000&end=1338465600000");
    }

    @Ignore
    @Test
    public void getWithInvalidTimeIntervalReturns400() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/occurrences?eventId=2&start=1325764800000&end=aroundmidnight");
    }

    @Test
    public void postJsonWithoutIdCorrectlyCreatesEventOccurrenceAndProduces201() {
        DateTime sampleStartDate = new DateTime(2012, 12, 21, 12, 30);
        DateTime sampleEndDate = new DateTime(2012, 12, 21, 16, 35);

        given().
            contentType("application/json").
            body("{\"event\":{\"id\":1},\"venue\":{\"id\":1},\"start\":" + sampleStartDate.getMillis()
                    + ",\"end\":" + sampleEndDate.getMillis() + "}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/wutup/occurrences/11").
            body(containsString("11")).
            contentType("application/json").
        when().
            post("/wutup/occurrences");
    }

    @Test
    public void postJsonWithIdCorrectlyIgnoresIdAndProduces201() {
        DateTime sampleStartDate = new DateTime(2012, 12, 21, 12, 30);
        DateTime sampleEndDate = new DateTime(2012, 12, 21, 16, 35);

        given().
            contentType("application/json").
            body("{\"id\":300,\"event\":{\"id\":1},\"venue\":{\"id\":1},\"start\":" +
                    sampleStartDate.getMillis() + ",\"end\":" + sampleEndDate.getMillis() + "}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/wutup/occurrences/12").
            body(containsString("12")).
            contentType("application/json").
        when().
            post("/wutup/occurrences");
    }

    @Test
    public void postJsonWithoutEventResponds400() {
        DateTime sampleStartDate = new DateTime(2012, 12, 21, 12, 30);
        DateTime sampleEndDate = new DateTime(2012, 12, 21, 16, 35);

        given().
            contentType("application/json").
            body("{\"venue\":{\"id\":1},\"start\":" + sampleStartDate.getMillis()
                    + ",\"end\":" + sampleEndDate.getMillis() + "}").
        expect().
            statusCode(400).
        when().
            post("/wutup/occurrences");
    }

    @Test
    public void postJsonWithoutVenueResponds400() {
        DateTime sampleStartDate = new DateTime(2012, 12, 21, 12, 30);
        DateTime sampleEndDate = new DateTime(2012, 12, 21, 16, 35);

        given().
            contentType("application/json").
            body("{\"event\":{\"id\":1},\"start\":" + sampleStartDate.getMillis()
                    + ",\"end\":" + sampleEndDate.getMillis() + "}").
        expect().
            statusCode(400).
        when().
            post("/wutup/occurrences");
    }

    @Test
    public void postJsonWithoutStartAndEndTimeProduces201() {
        given().
        contentType("application/json").
            body("{\"event\":{\"id\":1},\"venue\":{\"id\":1}}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/wutup/occurrences/13").
            body(containsString("13")).
            contentType("application/json").
        when().
            post("/wutup/occurrences");
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
    public void deleteEventOccurrenceProduces204() {
        given().
            contentType("application/json").
        expect().
            statusCode(204).
        when().
            delete("/wutup/occurrences/7");
    }

    @Test
    public void deleteNonExistentEventOccurrenceResponds404() {
        given().
            contentType("application/json").
        expect().
            statusCode(404).
        when().
            delete("/wutup/occurrences/8675309");
    }

    @Test
    public void getAttendeesByEventOccurrenceFindsAttendeesAndProduces200() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"facebookId\":\"hybridfbid\"")).
            body(containsString("\"id\":2")).
            body(containsString("\"email\":\"naked@winterfell.com\"")).
        when().
            get("/wutup/occurrences/1/attendees");
    }

    @Test
    public void getAttendeesFromNonExistentEventOccurrenceResponds200() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
        when().
            get("/wutup/occurrences/8675309/attendees");
    }

    @Test
    public void registerAttendeeToEventOccurrenceProduces204() {
        given().
            contentType("application/json").
            body("3").
        expect().
            statusCode(204).
        when().
            post("/wutup/occurrences/3/attendees");
    }

    @Test
    public void registerDuplicateAttendeeToEventOccurrenceProduces409() {
        given().
            contentType("application/json").
            body("1").
        expect().
            statusCode(409).
        when().
            post("/wutup/occurrences/1/attendees");
    }

    @Test
    public void registerNonExistentUserAsAttendeeToEventOccurrenceProduces404() {
        given().
            contentType("application/json").
            body("8675309").
        expect().
            statusCode(404).
        when().
            post("/wutup/occurrences/1/attendees");
    }

    @Test
    public void unregisterAttendeeToEventOccurrenceProduces204() {
        given().
            contentType("application/json").
        expect().
            statusCode(204).
        when().
            delete("/wutup/occurrences/1/attendees/2");
    }

    @Test
    public void unregisterNonRegisteredAttendeeResponds404() {
        given().
            contentType("application/json").
        expect().
            statusCode(404).
        when().
            delete("/wutup/occurrences/5/attendees/5");
    }

    @Test
    public void unregisterNonExistentUserFromAttendeesResponds404() {
        given().
            contentType("application/json").
        expect().
            statusCode(404).
        when().
            delete("/wutup/occurrences/5/attendees/8675309");
    }

    // ********************** Comment Testing *************************

    @Test
    public void testGetCommentsCanBeRead() {
        DateTime knownPostDate = new DateTime(2012, 4, 18, 0, 0, 0);
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"author\":{\"id\":3503,\"email\":\"jlennon@gmail.com\",\"nickname\":\"John\",\"firstname\":\"John\",\"lastname\":\"Lennon\"}")).
            body(containsString("\"body\":\"Aww yeah.\"")).
            body(containsString("\"postdate\":" + knownPostDate.getMillis())).
            body(containsString("\"id\":2")).
            body(containsString("\"body\":\"Aww no.\"")).
        when().
            get("/wutup/occurrences/1/comments");
    }

    @Test
    public void addedCommentCanBeFoundAndRead() {
        DateTime publishDate = new DateTime(2012, 11, 15, 12, 34, 56);
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(equalTo("[]")).
        when().
            get("/wutup/occurrences/3/comments");

        given().
            contentType("application/json").
            body("{\"author\":{\"id\":3},\"body\":\"aww hell nah\",\"postdate\":" + publishDate.getMillis() + "}").
        expect().
            statusCode(204).
        when().
            post("/wutup/occurrences/3/comments");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"email\":\"jh1942@lion.lmu.edu\"")).
            body(containsString("\"body\":\"aww hell nah\"")).
            body(containsString("\"postdate\":" + publishDate.getMillis())).
        when().
            get("/wutup/occurrences/3/comments");
    }

    @Test
    public void deletedOccurrenceCommentCanNoLongerBeFound() {
        DateTime knownPostDate = new DateTime(2012, 4, 18, 0, 0, 0);
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":3")).
            body(containsString("\"author\":{\"id\":3503,\"email\":\"jlennon@gmail.com\",\"nickname\":\"John\",\"firstname\":\"John\",\"lastname\":\"Lennon\"}")).
            body(containsString("\"body\":\"Aww yeah.\"")).
            body(containsString("\"postdate\":" + knownPostDate.getMillis())).
        when().
            get("/wutup/occurrences/5/comments");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(204).
        when().
            delete("/wutup/occurrences/5/comments/3");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(equalTo("[]")).
        when().
            get("/wutup/occurrences/5/comments");
    }

    @Test
    public void deleteNonExistantOccurrenceCommentResponds404() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(404).
        when().
            delete("/wutup/occurrences/6/comments/666");
    }

    @Test
    public void deleteCommentOnNonexistantOccurrenceResponds404() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(404).
        when().
            delete("/wutup/occurrences/56/comments/3");
    }

    @Test
    public void addCommentToNonExistantOccurrenceResponds404() {
        DateTime publishDate = new DateTime(2012, 11, 15, 12, 34, 56);
        given().
            contentType("application/json").
            body("{\"author\":{\"id\":3},\"body\":\"aww hell nah\",\"postdate\":" + publishDate.getMillis() + "}").
        expect().
            statusCode(404).
        when().
            post("/wutup/occurrences/26/comments");
    }

    @Test
    public void addCommentWithNonExistantAuthorResponds404() {
        DateTime publishDate = new DateTime(2012, 11, 15, 12, 34, 56);
        given().
            contentType("application/json").
            body("{\"author\":{\"id\":666},\"body\":\"aww hell nah\",\"postdate\":" + publishDate.getMillis() + "}").
        expect().
            statusCode(404).
        when().
            post("/wutup/occurrences/26/comments");
    }
}
