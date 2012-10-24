package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

public class UserResourceIT {

    @Test
    public void getUserRespondsProperly() {
        expect()
            .statusCode(200)
        .contentType("application/json")
            .body(containsString("\"id\":1"))
            .body(containsString("\"nickname\":\"hybrid\""))
        .when()
            .get("/wutup/users/1");
    }

    @Test
    public void createUserOnPostToUsersResourceReturns201() {
        given()
            .contentType("application/json")
            .body("{\"id\":155,\"firstname\":\"Carlos\",\"lastname\":\"Agudo\""
                    + ",\"nickname\":\"Big Sexy\",\"email\":\"cagudo@gmail.com\"}")
        .expect()
            .statusCode(201)
        .when()
            .post("/wutup/users");
    }
    
    @Test
    public void updateUserResourceReturns204OnSuccess() {
        given()
            .contentType("application/json")
            .body("{\"id\":2,\"firstname\":\"Ted\",\"lastname\":\"Stark\""
                    + ",\"nickname\":\"headless\",\"email\":\"naked@winterfell.com\"}")
        .expect()
            .statusCode(204)
        .when()
            .put("/wutup/users/2");
    }

}
