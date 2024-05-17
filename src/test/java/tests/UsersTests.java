package tests;

import config.Config;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Constants;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static utils.Constants.*;

public class UsersTests extends Config {


    @Test
    public void getUsersTest() {
        Map<String, Integer> map = new HashMap<>();
        map.put("page", 1);
        map.put("limit", 5);

        Response response = given()
                .queryParams(map)
                .when().get(Constants.GET_ALL_USERS);


        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String actualFirstName = response.jsonPath().get("data[0].firstName");
        System.out.println(actualFirstName);

        Assert.assertEquals(actualFirstName, "Carolina");

    }

    @Test
    public void getUsersUsingJsonPathTest() {
        Map<String, Integer> map = new HashMap<>();
        map.put("page", 1);
        map.put("limit", 5);

        JsonPath jsonPath = given()
                .queryParams(map)
                .when().get(Constants.GET_ALL_USERS).getBody().jsonPath();

        String actualFirstName = jsonPath.getString("data[0].firstName");
        boolean result = actualFirstName.equals("Carolina");

        Assert.assertTrue(result, "Expected first name is not correct.");
    }

    @Test
    public void getUserByIdTest() {

        String userId = "6643977220862a0f17b9f76b";
        Response response = given()
                .pathParam("id", userId)
                .when().get(GET_USER_BY_ID);


        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String actualFirstName = response.jsonPath().get("firstName");
        System.out.println(actualFirstName);

        Assert.assertEquals(actualFirstName, "Test");

    }


    @Test
    public void deleteUserByIdTest() {

        String userId = "60d0fe4f5311236168a109db";
        Response response = given()
                .pathParam("id", userId)
                .when().delete(DELETE_USER_BY_ID);


        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String id = response.jsonPath().get("id");
        System.out.println(id);

        Assert.assertEquals(id, userId);

        given()
                .pathParam("id", userId)
                .when().delete(DELETE_USER_BY_ID);
        Assert.assertEquals(response.getStatusCode(), 404, "Expected 404 but got: " + response.getStatusCode());
    }


    @Test
    public void createUserTest() {

        Response response = given()
                .body("{\n" +
                        "    \"title\": \"miss\",\n" +
                        "    \"firstName\": \"Naomi\",\n" +
                        "    \"lastName\": \"Rodrigues\",\n" +
                        "    \"picture\": \"https://randomuser.me/api/portraits/med/women/39.jpg\",\n" +
                        "    \"gender\": \"female\",\n" +
                        "    \"email\": \"naomi.rodrigues1222321@example.com\",\n" +
                        "    \"dateOfBirth\": \"1973-06-13T23:33:31.385Z\",\n" +
                        "    \"phone\": \"(40) 6623-4814\",\n" +
                        "    \"location\": {\n" +
                        "        \"street\": \"9134, Rua Castro Alves \",\n" +
                        "        \"city\": \"Garanhuns\",\n" +
                        "        \"state\": \"Roraima\",\n" +
                        "        \"country\": \"Brazil\",\n" +
                        "        \"timezone\": \"+9:00\"\n" +
                        "    }\n" +
                        "}")
                .when().post(CREATE_USER);

        Assert.assertEquals(response.getStatusCode(), 200);

    }




}
