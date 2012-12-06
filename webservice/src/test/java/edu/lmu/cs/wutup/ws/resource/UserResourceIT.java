package edu.lmu.cs.wutup.ws.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.joda.time.DateTime;
import org.junit.Test;

public class UserResourceIT {

    @Test
    public void testGetUserById() {
        given()
            .contentType("application/json")
        .expect()
            .statusCode(200)
            .contentType("application/json")
            .body(containsString("\"id\":5"))
            .body(containsString("\"nickname\":\"IKOK\""))
            .body(containsString("\"email\":\"iggy@hotmail.com\""))
            .body(containsString("\"firstname\":\"Ignatius\""))
            .body(containsString("\"lastname\":\"Krumpkin\""))
        .when()
            .get("wutup/users/5");
    }

    @Test
    public void testGetNonExistantUserByIdResponds404() {
        given().
            contentType("application/json").
        expect().
            statusCode(404).
        when().
            get("/wutup/users/12344");
    }

    @Test
    public void testGetUserWithMalformedIdResponds400() {
        given().
            contentType("application/json").
        expect().
            statusCode(400).
        when().
            get("/wutup/users/hellonurse");
    }

    @Test
    public void testCreateUserRequiresAtleastNameAndEmail() {
        given()
            .contentType("application/json")
            .body("{\"id\":155,\"firstname\":\"Carlos\",\"lastname\":\"Agudo\""
                    + ",\"nickname\":\"Big Sexy\"}")
        .expect()
            .statusCode(400)
        .when()
            .post("/wutup/users");

        given()
            .contentType("application/json")
            .body("{\"id\":155,\"lastname\":\"Agudo\""
                    + ",\"nickname\":\"Big Sexy\",\"email\":\"cagudo@gmail.com\"}")
        .expect()
            .statusCode(400)
        .when()
            .post("/wutup/users");

        given()
            .contentType("application/json")
            .body("{\"id\":155,\"firstname\":\"Carlos\","
                    + ",\"nickname\":\"Big Sexy\",\"email\":\"cagudo@gmail.com\"}")
        .expect()
            .statusCode(400)
        .when()
            .post("/wutup/users");
    }

    @Test
    public void createUserOnPostToUsersResourceResponds201WithLocation() {
        given()
            .contentType("application/json")
            .body("{\"id\":155,\"firstname\":\"Carlos\",\"lastname\":\"Agudo\""
                    + ",\"nickname\":\"Big Sexy\",\"email\":\"cagudo@gmail.com\"}")
        .expect()
            .statusCode(201)
            .header("Location", "http://localhost:8080/wutup/users/3505")
        .when()
            .post("/wutup/users");
    }

    @Test
    public void postedUserCanBeRead() {
        given()
            .contentType("application/json")
            .body("{\"firstname\":\"Cake\",\"lastname\":\"Man\""
                    + ",\"nickname\":\"Funfetti\",\"email\":\"party@party.com\"}")
        .expect()
            .statusCode(201)
            .header("Location", "http://localhost:8080/wutup/users/3506")
        .when()
            .post("/wutup/users");

        given()
            .contentType("application/json")
        .expect()
            .statusCode(200)
            .contentType("application/json")
            .body(containsString("\"id\":3506"))
            .body(containsString("\"nickname\":\"Funfetti\""))
            .body(containsString("\"email\":\"party@party.com\""))
            .body(containsString("\"firstname\":\"Cake\""))
            .body(containsString("\"lastname\":\"Man\""))
        .when()
            .get("wutup/users/3506");
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

    @Test
    public void deleteNonExistantUserResponds404() {
        given().
            contentType("application/json").
        expect().
            statusCode(404).
        when().
            delete("/wutup/users/88434");
    }

    @Test
    public void deletedUserCanNoLongerBeFound() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":3504")).
            body(containsString("\"firstname\":\"Sam\"")).
            body(containsString("\"lastname\":\"Verhasselt\"")).
            body(containsString("\"email\":\"azureus42@yahoo.com\"")).
            body(containsString("\"nickname\":\"Sam Verhasselt\"")).
            body(containsString("\"facebookId\":\"777892175\"")).
        when().
            get("/wutup/users/3504");

        given().
            contentType("application/json").
        expect().
            statusCode(204).
        when().
            delete("/wutup/users/3504");

        given().
            header("Accept", "application/json").
        expect().
            statusCode(404).
        when().
            get("/wutup/users/3504");
    }

    @Test
    public void findCommentsByNonExistantUserIdResponds404() {
        given()
            .header("Accept", "application/json")
        .expect()
            .statusCode(404)
        .when()
            .get("wutup/users/9999/comments");

    }

    @Test
    public void findCommentsByExistingUserIdResponds200OnSuccess() {
        given()
            .header("Accept", "application/json")
        .expect()
            .statusCode(200)
        .when()
            .get("wutup/users/1/comments");
    }

    @Test
    public void commentsFoundByUserIdCanBeRead() {
        DateTime publishDateFirst = new DateTime(2012, 3, 17, 0, 0, 0);
        DateTime publishDateSecond = new DateTime(2012, 3, 30, 12, 34, 56);
        DateTime publishDateThird = new DateTime(2012, 12, 25, 7, 0, 0);
        given()
            .header("Accept", "application/json")
        .expect()
            .statusCode(200)
            .body(containsString("\"email\":\"40mpg@gmail.com\""))
            .body(containsString("\"postdate\":" + publishDateFirst.getMillis() + "}"))
            .body(containsString(publishDateSecond.getMillis() + "}"))
            .body(containsString(publishDateThird.getMillis() + "}"))
        .when()
            .get("wutup/users/1/comments");
    }

    @Test
    public void findCommentsByUserWithNoCommentsRespondsEmptyArray() {
        given()
            .header("Accept", "application/json")
        .expect()
            .statusCode(200)
            .body(equalTo("[]"))
        .when()
            .get("wutup/users/7/comments");
    }
}
