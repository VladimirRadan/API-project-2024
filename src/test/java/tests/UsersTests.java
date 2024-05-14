package tests;

import endpoints.UserEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UsersTests {




    @Test
    public void getUsersTest() {
        Map<String, Integer> map = new HashMap<>();
        map.put("page", 1);
        map.put("limit", 5);

        Response response = given()
                .baseUri("https://dummyapi.io/data")
                .basePath("/v1/")
                .header("app-id", "628e9aa43e2c54581058e8aa")
                .queryParams(map)
                .log().all()
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
                .baseUri("https://dummyapi.io/data")
                .basePath("/v1/")
                .header("app-id", "628e9aa43e2c54581058e8aa")
                .queryParams(map)
                .log().all()
                .when().get(Constants.GET_ALL_USERS).getBody().jsonPath();

        String actualFirstName = jsonPath.getString("data[0].firstName");
        boolean result = actualFirstName.equals("Carolina");

        Assert.assertTrue(result, "Expected first name is not correct.");
    }

    @Test
    public void getUserByIdTest() {

        Response response = given()
                .baseUri("https://dummyapi.io/data")
                .basePath("/v1/")
                .header("app-id", "628e9aa43e2c54581058e8aa")
                .log().all()
                .when().get(Constants.GET_USER_BY_ID);


        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String actualFirstName = response.jsonPath().get("data[0].firstName");
        System.out.println(actualFirstName);

        Assert.assertEquals(actualFirstName, "Carolina");

    }



}
