package tests;

import config.Config;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import listeners.TestListener;
import org.testng.annotations.Listeners;
import models.userModel.UserRequest;
import models.userModel.UserResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.Constants;
import utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static utils.Constants.*;
import static utils.Utils.createJsonFile;
@Listeners(TestListener.class)
public class UsersTests extends Config {

    SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void setup(){
        softAssert = new SoftAssert();
    }

    @Test(description = "Get all users from DB; Expected result: All users are retrieved", groups = "smoke, regression")
    public void getUsersTest() {
        Map<String, Integer> map = new HashMap<>();
        map.put("page", 3);
        map.put("limit", 50);

        UserRequest userRequest = UserRequest.createUser();

        UserResponse userResponse = given()
                .body(userRequest)
                .when().post(CREATE_USER).getBody().as(UserResponse.class);

        List<UserResponse> response = given()
                .queryParams(map)
                .when().get(Constants.GET_ALL_USERS).jsonPath().getList("data", UserResponse.class);

        String expectedId = userResponse.getId();

        boolean isInTheList = false;
        for (int i = 0; i < response.size(); i++) {
            if (response.get(i).getId().equals(expectedId)) {
                isInTheList = true;
            }

            Assert.assertTrue(isInTheList);

        }
    }

    @Test(groups = "regression")
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

    @Test(enabled = false)
    public void getUserByIdTest() {

        UserRequest userRequest = UserRequest.createUser();

        UserResponse userResponse = given()
                .body(userRequest)
                .when().post(CREATE_USER).getBody().as(UserResponse.class);

        String userId = userResponse.getId();
        Response response = given()
                .pathParam("id", userId)
                .when().get(GET_USER_BY_ID);


        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String actualFirstName = response.jsonPath().get("firstName");
        System.out.println(actualFirstName);

        Assert.assertEquals(actualFirstName, "Test");
    }


    @Test()
    public void deleteUserByIdTest() {

        UserRequest userRequest = UserRequest.createUser();

        UserResponse userResponse = given()
                .body(userRequest)
                .when().post(CREATE_USER).getBody().as(UserResponse.class);

        String userId = userResponse.getId();
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

        createJsonFile("userRequest", userRequest);

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

    @Test
    public void readFromJson(){
        UserResponse userResponse = Utils.getUserFromJson("userRequest");
        System.out.println(userResponse);
    }


}
