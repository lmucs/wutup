package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.joda.time.DateTime;
import org.junit.Test;

public class EventResourceIT {

    @Test
    public void endpointGetByIdFindsExistingEvent() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"name\":\"Poker Night\"")).
            body(containsString("\"description\":\"Cards with the guys\"")).
            body(containsString("Katrina")).
        when().
            get("/wutup/events/1");
    }

    @Test
    public void endpointGetByIdWithUnusedIdProduces404() {
        expect().
            statusCode(404).
            body(containsString("Event 100")).
        when().
            get("/wutup/events/100");
    }

    @Test
    public void getEventsByQueryCanRespondEmptyArray() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(equalTo("[]")).
        when().
            get("/wutup/events?owner=8008135,2626");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(equalTo("[]")).
        when().
            get("/wutup/events?name=zubatz");
    }

    @Test
    public void endpointGetByNameFindsExistingEvent() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"name\":\"Poker Night\"")).
            body(containsString("\"description\":\"Cards with the guys\"")).
            body(containsString("Katrina")).
        when().
            get("/wutup/events?name=Poker");
    }

    @Test
    public void endpointGetByPartialNameFindsExistingEvent() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"name\":\"Poker Night\"")).
            body(containsString("\"description\":\"Cards with the guys\"")).
            body(containsString("Katrina")).
        when().
            get("/wutup/events?name=Pok");
    }

    @Test
    public void endpointGetByOwnerFindsExistingEvent() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"name\":\"Poker Night\"")).
            body(containsString("\"description\":\"Cards with the guys\"")).
            body(containsString("Katrina")).
        when().
            get("/wutup/events?owner=8");
    }

    @Test
    public void endpointGetByMultipleOwnersFindsExistingEvents() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"name\":\"Poker Night\"")).
            body(containsString("\"id\":2")).
            body(containsString("\"name\":\"Billiards with Prince Harry\"")).
            body(containsString("Katrina")).
        when().
            get("/wutup/events?owner=8,7");
    }

    @Test
    public void endpointGetHasJsonAsDefaultAcceptHeader() {
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"name\":\"Poker Night\"")).
            body(containsString("\"description\":\"Cards with the guys\"")).
            body(containsString("Katrina")).
        when().
            get("/wutup/events/1");
    }

    @Test
    public void endpointPostJsonCorrectlyCreatesEventAndProduces201() {
        given().
            contentType("application/json").
            body("{\"name\":\"Ski Trip\",\"description\":\"WOOT\",\"creator\":{\"id\":8}}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/wutup/events/9").
            contentType("application/json").
        when().
            post("/wutup/events");
    }

    @Test
    public void endpointPostToEventsWithBadEventResponds400() {
        given().
            contentType("application/json").
            body("{\"creator\":{\"id\":8},\"description\":\"TALLY HO\"}").
        expect().
            statusCode(400).
        when().
            post("/wutup/events");

        given().
            contentType("application/json").
            body("{\"name\":\"Cheers, gentlemen.\"\"description\":\"TALLY HO\"}").
        expect().
            statusCode(400).
        when().
            post("/wutup/events");

        given().
            contentType("application/json").
            body("{}").
        expect().
            statusCode(400).
        when().
            post("/wutup/events");
    }

    @Test
    public void postedEventCanBeReadAndContainsLocationInHeader() {
        given().
            contentType("application/json").
            body("{\"name\":\"Cheers, Gentlemen\",\"description\":\"TALLY HO\",\"creator\":{\"id\":8}}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/wutup/events/10").
            contentType("application/json").
        when().
            post("/wutup/events");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"name\":\"Cheers, Gentlemen\"")).
            body(containsString("\"id\":10")).
            body(containsString("\"description\":\"TALLY HO\"")).
            body(containsString("\"firstname\":\"Katrina\"")).
            body(containsString("\"lastname\":\"Sherbina\"")).
            body(containsString("\"email\":\"ksherbina@gmail.com\"")).
            body(containsString("\"firstname\":\"Katrina\"")).
            body(containsString("\"nickname\":\"Kat\"")).
        when().
            get("/wutup/events/10");
    }

    @Test
    public void endpointPatchWithUnusedIdProduces404() {
        given().
            contentType("application/json").
            body("{\"id\":\"100\",\"name\":\"Ski Trip\"}").
        expect().
            statusCode(404).
        when().
            patch("/wutup/events/100");
    }

    @Test
    public void endpointPatchWithMismatchedIdProduces409() {
        given().
            contentType("application/json").
            body("{\"id\":172,\"name\":\"Ski Trip\"}").
        expect().
            statusCode(409).
        when().
            patch("/wutup/events/46");
    }

    @Test
    public void endpointGetAfterUpdateIsCorrect() {
        given().
            contentType("application/json").
            body("{\"name\":\"Texas Hold Em\"}").
        expect().
            statusCode(204).
        when().
            patch("/wutup/events/8");

        given().
            contentType("application/json").
        expect().
            statusCode(200).
            body(containsString("\"name\":\"Texas Hold Em\"")).
            body(containsString("\"description\":\"Don't miss out\"")).
            body(containsString("\"id\":8")).
        when().
            get("/wutup/events/8");
    }

    @Test
    public void endpointDeleteWithUnusedIdProduces404() {
        given().
            contentType("application/json").
        expect().
            statusCode(404).
        when().
            delete("/wutup/events/100");
    }

    @Test
    public void endpointDeleteRemovesEvents() {
        given().
            contentType("application/json").
        expect().
            statusCode(204).
        when().
            delete("/wutup/events/6");

        given().
            contentType("application/json").
        expect().
            statusCode(404).
            body(containsString("Event 6")).
        when().
            get("/wutup/events/6");
    }

    // *********************** Comment Testing ***********************

    @Test
    public void endpointGetEventCommentsFindsExistingComments() {
        Long knownTimestamp = new DateTime(2012, 3, 17, 0, 0).getMillis();
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
            body(containsString("\"body\":\"Boo, sux\"")).
            body(containsString("\"postdate\":" + knownTimestamp)).
        when().
            get("/wutup/events/1/comments");
    }

    @Test
    public void endpointAddCommentToEventCanBeFound() {
        DateTime publishDate = new DateTime(2012, 12, 21, 0, 0);
        given().
            contentType("application/json").
            body("{\"body\":\"Hello, there!\",\"author\":{\"id\":3,\"email\":\"jh1942@lion.lmu.edu\"," +
                    "\"nickname\":\"DeepThoughts\",\"firstname\":\"Jack\",\"lastname\":\"Handy\"},\"postdate\":" + publishDate.getMillis() + "}").
        expect().
            statusCode(204).
        when().
            post("/wutup/events/4/comments");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(containsString("\"body\":\"Hello, there!\"")).
            body(containsString("\"author\":{\"id\":3,\"email\":\"jh1942@lion.lmu.edu\",\"nickname\":\"DeepThoughts\"," +
                    "\"firstname\":\"Jack\",\"lastname\":\"Handy\"}")).
            body(containsString("\"postdate\":" + publishDate.getMillis())).
            body(containsString("\"id\":3")).
        when().
            get("/wutup/events/4/comments");

    }

    @Test
    public void endpointDeleteEventCommentCanNoLongerBeFound() {
        Long knownTimestamp = new DateTime(2012, 3, 17, 0, 0).getMillis();
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":2")).
            body(containsString("\"body\":\"Boo, sux\"")).
            body(containsString("\"postdate\":" + knownTimestamp)).
        when().
            get("/wutup/events/2/comments");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(204).
        when().
            delete("/wutup/events/2/comments/2");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            body(equalTo("[]")).
        when().
            get("/wutup/events/2/comments");
    }

    @Test
    public void deleteNonExistantCommentResponds404() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(404).
        when().
            delete("/wutup/events/1/comments/56");
    }
}
