package tests;

import com.github.javafaker.Faker;
import config.Config;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.UserLocation;
import model.UserRequest;
import model.UserResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.Constants;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static utils.Constants.*;

public class UsersTests extends Config {

    SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void setup(){
        softAssert = new SoftAssert();
    }

    @Test
    public void getUsersTest() {
        Map<String, Integer> map = new HashMap<>();
        map.put("page", 1);
        map.put("limit", 5);

        Response response = given()
                .queryParams(map)
                .when().get(Constants.GET_ALL_USERS);


        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String actualFirstName = response.jsonPath().get("data[0].firstName");
        System.out.println(actualFirstName);

        softAssert.assertEquals(actualFirstName, "Carolina");
        softAssert.assertAll();
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

        String userId = "60d0fe4f5311236168a109d2";
        Response response = given()
                .pathParam("id", userId)
                .when().delete(DELETE_USER_BY_ID);


        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String id = response.jsonPath().get("id");
        System.out.println(id);

        Assert.assertEquals(id, userId);

        Response errorResponse = given()
                .pathParam("id", userId)
                .when().delete(DELETE_USER_BY_ID);
        Assert.assertEquals(errorResponse.getStatusCode(), 404, "Expected 404 but got: " + errorResponse.getStatusCode());
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

    @Test
    public void createUserUsingJavaObjectTest() {

        UserRequest userRequest = UserRequest.createUser();

        UserResponse userResponse = given()
                .body(userRequest)
                .when().post(CREATE_USER).getBody().as(UserResponse.class);

        softAssert.assertEquals(userResponse.getEmail(), userRequest.getEmail());
        softAssert.assertEquals(userResponse.getFirstName(), userRequest.getFirst_name());
        softAssert.assertEquals(userResponse.getLastName(), userRequest.getLastName());
        softAssert.assertEquals(userResponse.getLocation().getStreet(), userRequest.getUserLocation().getStreet());
        softAssert.assertAll();

    }

    @Test
    public void updateUserTest() {

        UserRequest user = UserRequest.createUser();

        UserResponse userResponse = given()
                .body(user)
                .when().post(CREATE_USER).getBody().as(UserResponse.class);

        String updatedFirstName = "updatedFirstName";
        String updatedEmail = "updatedEmail";
        String updatedCity = "updatedCity";

//        userRequest.setFirst_name(updatedFirstName);
//        userRequest.setEmail(updatedEmail);
//        userRequest.getUserLocation().setCity(updatedCity);

        UserRequest updatedUser = user
                .withFirst_name(updatedFirstName)
                .withEmail(updatedEmail)
                .withUserLocation(user.getUserLocation().withCity(updatedCity));

        String userId = userResponse.getId();

        UserResponse updatedUserResponse = given()
                .body(updatedUser)
                .pathParam("id", userId)
                .when().put(UPDATE_USER).getBody().as(UserResponse.class);

        boolean isFirstNameUpdated = updatedUserResponse.getFirstName().equals(updatedFirstName);

        //softAssert.assertEquals(updatedUserResponse.getFirstName(), updatedFirstName);
        softAssert.assertTrue(isFirstNameUpdated, "First name not updated");
        //softAssert.assertEquals(updatedUserResponse.getEmail(), updatedEmail, "Email not updated");
        softAssert.assertEquals(updatedUserResponse.getLocation().getCity(), updatedCity, "City not updated");
        softAssert.assertAll();
    }



}
