package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.joda.time.DateTime;
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
    public void endpointGetWithTimeIntervalQueryFindsExistingEventOccurrences() {
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
            body(containsString("\"id\":4")).
            body(containsString("\"author\":{\"id\":3,\"email\":\"jh1942@lion.lmu.edu\",\"nickname\":\"DeepThoughts\",\"firstname\":\"Jack\",\"lastname\":\"Handy\"}")).
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
