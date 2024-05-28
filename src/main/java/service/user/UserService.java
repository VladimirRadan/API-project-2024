package service.user;

import api.ApiClient;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import models.userModel.UserLocation;
import models.userModel.UserRequest;
import models.userModel.UserResponse;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class UserService {

    private static final String GET_ALL_USERS = "user";
    private static final String GET_USER_BY_ID = "user/{id}";
    private static final String DELETE_USER_BY_ID = "user/{id}";
    private static final String CREATE_USER = "user/create";
    private static final String UPDATE_USER = "/user/{id}";

    private ApiClient apiClient = new ApiClient();

    public static UserRequest createUserTemplate(){
        Faker faker = new Faker(new Locale("en-US"));

        UserLocation location = UserLocation.builder()
                .city(faker.address().city())
                .state(faker.address().state())
                .street(faker.address().streetAddress())
                .country(faker.address().country())
                .build();

        return UserRequest.builder()
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .first_name(faker.name().firstName())
                .lastName(faker.name().lastName())
                .userLocation(location)
                .build();
    }

    public UserResponse createUser(UserRequest user){
        return apiClient.post(CREATE_USER, user).getBody().as(UserResponse.class);
    }









}
