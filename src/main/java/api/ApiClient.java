package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private String BASE_URL = "https://dummyapi.io/data";
    private String BASE_PATH = "/v1/";

    static {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.defaultParser = Parser.JSON;
    }


    private RequestSpecification defaultRequestSpec(){
        Map<String, String> headers = new HashMap<>();
        headers.put("app-id", "628e9aa43e2c54581058e8aa");
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return new RequestSpecBuilder()
                .setRelaxedHTTPSValidation()
                .setBaseUri(BASE_URL)
                .setBasePath(BASE_PATH)
                .addHeaders(headers)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    public Response post(String endpoint, Object body){
        return given()
                .spec(defaultRequestSpec())
                .body(body)
                .when()
                .post(endpoint);
    }

    public Response put(String endpoint, Object body){
        return given()
                .spec(defaultRequestSpec())
                .body(body)
                .when()
                .put(endpoint);
    }





}
