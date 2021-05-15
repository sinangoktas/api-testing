package twitter.com.statuses;

import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import twitter.com.constants.Auth;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.lessThan;

public class Demo3 {

    // Request Specifications

    String consumerKey = Auth.CONSUMER_KEY;
    String consumerSecret = Auth.CONSUMER_SECRET;
    String accessToken = Auth.ACCESS_TOKEN;
    String accessSecret = Auth.ACCESS_SECRET;

    RequestSpecBuilder requestBuilder;
    static RequestSpecification requestSpec;

    ResponseSpecBuilder responseBuilder;
    static ResponseSpecification responseSpec;


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.twitter.com";
        RestAssured.basePath = "/1.1/statuses";

        requestBuilder = new RequestSpecBuilder();
        requestBuilder.setBaseUri("https://api.twitter.com");
        requestBuilder.setBasePath("/1.1/statuses");
        requestBuilder.addQueryParam("user_id", "restAutomation");
        AuthenticationScheme authScheme = RestAssured.oauth(consumerKey, consumerSecret, accessToken, accessSecret);
        requestBuilder.setAuth(authScheme);
        requestSpec = requestBuilder.build();


        responseBuilder = new ResponseSpecBuilder();
        responseBuilder.expectStatusCode(200);
        responseBuilder.expectResponseTime(lessThan(1L), TimeUnit.SECONDS);
        responseBuilder.expectBody("user.name", hasItem("Sinan Goktas"));
        responseBuilder.expectBody("user.screen_name", hasItem("restAutomation"));
        responseSpec = responseBuilder.build();


    }


    // Response Specifications

    @Test()
    public void testRequestSpecs() {

        given()
                .spec(requestSpec)
//                .auth()
//                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
//                .queryParam("user_id", "restAutomation")
                .when()
                .get("/user_timeline.json")
                .then()
                .statusCode(200);

    }


    @Test()
    public void testResponseSpecs() {
        given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("user_id", "restAutomation")
                .when()
                .get("/user_timeline.json")
                .then()
                .spec(responseSpec);

    }

    @Test()
    public void testClubRequestResponse() {
        given()
                .spec(requestSpec)
                .when()
                .get("\"/user_timeline.json\"")
                .then()
                .spec(responseSpec);

    }

}
