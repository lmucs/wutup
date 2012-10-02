package edu.lmu.cs.wutup.ws.resource;


public class CommentResourceIT {
/*
    @Test
    public void endpointGetFindsExistingCommentJson() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
        when().
            get("/wutup/comments/1");
    }

    @Test
    public void endpointGetFindsExistingCommentXML() {
        expect().
            statusCode(200).
            contentType("application/xml").
            body(containsString("<id>1</id>")).
            body(containsString("<comment>")).
        when().
            get("/wutup/comments/1");
    }

    @Test
    public void endpointGetWithUnusedIdProduces404() {
        expect().
            statusCode(404).
            body(containsString("Comment 100")).
        when().
            get("/wutup/comments/100");
    }

    @Test
    public void endpointPutWithUnusedIdProduces404() {
        given().
            header("Accept", "application/json").
            contentType("application/json").
            body("{\"id\":\"100\",\"user\":\"Ski Trip\"}").
        expect().
            statusCode(404).
        when().
            put("/wutup/comments/100");
    }

    @Test
    public void endpointPutWithMismatchedIdProduces409() {
        given().
            header("Accept", "application/json").
            contentType("application/json").
            body("{\"id\":\"172\",\"user\":\"Ski Trip\"}").
        expect().
            statusCode(409).
        when().
            put("/wutup/comments/46");
    }

    @Test
    public void endpointPostJsonCorrectlyCreatesCommentAndProduces201() {
        given().
            header("Accept", "application/json").
            contentType("application/json").
            body("{\"id\":\"9\",\"user\":\"Ski Trip\"}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/wutup/comments/9").
            contentType("application/json").
        when().
            post("/wutup/comments");
    }
    */
}
