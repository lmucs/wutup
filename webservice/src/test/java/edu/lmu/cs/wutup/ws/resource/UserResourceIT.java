package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

public class UserResourceIT {

    @Test
    public void createUserOnPostToUsersResourceResponds201WithLocation() {
        given()
            .contentType("application/json")
            .body("{\"id\":155,\"firstname\":\"Carlos\",\"lastname\":\"Agudo\""
                    + ",\"nickname\":\"Big Sexy\",\"email\":\"cagudo@gmail.com\"}")
        .expect()
            .statusCode(201)
            .header("Location", "http://localhost:8080/wutup/users/3504")
        .when()
            .post("/wutup/users");
    }

    @Test
    public void updateUserWithIdMismatchResponds409() {
        given()
            .contentType("application/json")
            .body("{\"id\":2,\"email\":\"noname@yahoo.com\"}")
        .expect()
            .statusCode(409)
        .when()
            .patch("wutup/users/1");
    }

    @Test
    public void retrieveUserByFbId() {
        given()
            .contentType("application/json")
        .expect()
            .statusCode(404)
        .when()
            .get("wutup/users?fbId=1");
    }

    @Test
    public void malformedGetByFbIdReturns400() {
        given()
            .contentType("application/json")
        .expect()
            .statusCode(400)
        .when()
            .get("wutup/users");
    }

    @Test
    public void updateUserWithoutIdInBodyResponds204OnSuccess() {
        given()
            .contentType("application/json")
            .body("{\"email\":\"test@user.com\"}")
        .expect()
            .statusCode(204)
        .when()
            .patch("wutup/users/7");
        
        expect()
            .statusCode(200)
            .contentType("application/json")
            .body(containsString("\"id\":7"))
            .body(containsString("\"nickname\":\"gaah\""))
            .body(containsString("\"email\":\"test@user.com\""))
            .body(containsString("\"firstname\":\"Olga\""))
            .body(containsString("\"lastname\":\"Shoopa\""))
        .when()
            .get("/wutup/users/7");
    }

    @Test
    public void getNonExistantUserResponds404() {
        given()
            .contentType("application/json")
        .expect()
            .statusCode(404)
        .when()
            .get("wutup/users/8008135");
    }

    @Test
    public void updateUserResourceReturns204OnSuccess() {
        given()
            .contentType("application/json")
            .body("{\"id\":2,\"firstname\":\"Ned\",\"lastname\":\"Stark\""
                    + ",\"nickname\":\"headless\",\"email\":\"naked@winterfell.com\"}")
        .expect()
            .statusCode(204)
        .when()
            .patch("/wutup/users/2");
    }

}
