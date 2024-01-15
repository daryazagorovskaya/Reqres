package api;

import io.restassured.http.ContentType;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class ReqresTest {

    public static final String URL = "https://reqres.in";
    private static final String LIST_OF_USERS_RESPONSE  = "src/test/resources/getListOfUsers";

    @Test
    public void create() {
        HashMap <String, String> user = new HashMap<>();
        user.put("name", "morpheus");
        user.put("job", "leader");
        given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(URL +  "/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", Matchers.notNullValue())
                .body("createdAt", Matchers.notNullValue());
    }

    @Test
    public void registerSuccessfulRequest() {
        HashMap <String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "pistol");
        given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(URL +  "/api/register")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(4))
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
        }

    @Test
    public void registerUnSuccessful() {
        given()
                .body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post(URL +  "/api/register")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void loginSuccessful() {
        given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post(URL +  "/api/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void loginUnsuccessful() {
        given()
                .body("{\n" +
                        "    \"email\": \"peter@klaven\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post(URL +  "/api/login")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void listUsers() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL +  "/api/users?page=2")
                .then()
                .log().all()
                .statusCode(200)
                .body(hasItems(new File(LIST_OF_USERS_RESPONSE)));
    }


    @Test
    public void singleUser() {
        HashMap <String, Object> user = new HashMap<>();
        user.put("id", 2);
        user.put("email", "janet.weaver@reqres.in");
        user.put("first_name", "Janet");
        user.put("last_name", "Weaver");
        user.put("avatar", "https://reqres.in/img/faces/2-image.jpg");
        given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL +  "/api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data", equalTo(user));
    }

    @Test
    public void singleUserNotFound() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL +  "/api/users/23")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    public void listResource() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL +  "api/users?page=2")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void singleResource() {
        HashMap <String, Object> user = new HashMap<>();
        user.put("id", 2);
        user.put("name", "fuchsia rose");
        user.put("year", 2001);
        user.put("color", "#C74375");
        user.put("pantone_value", "17-2031");
        given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL +  "/api/unknown/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data", equalTo(user));
    }

    @Test
    public void singleResourceNotFound() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL +  "/api/unknown/23")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    public void updatePut() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", "morpheus");
        user.put("job", "zion resident");
        given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .put(URL + "/api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .body("updatedAt", Matchers.notNullValue());
    }

    @Test
    public void updatePatch() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", "morpheus");
        user.put("job", "zion resident");
        given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .patch(URL + "/api/users/2")
        .then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .body("updatedAt", Matchers.notNullValue());
    }

    @Test
    public void delete() {
        given()
        .when()
                .delete(URL + "/api/users/2")
                .then()
                .log().all()
                .statusCode(204);
    }
    @Test
    public void delayedResponse() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL +  "/api/users?delay=3")
                .then()
                .log().all()
                .statusCode(200)
                //.body(hasItems(new File(LIST_OF_USERS_RESPONSE)));
    }
}
