package twitter.com.statuses;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import twitter.com.constants.Auth;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Demo2 {

    // Rest Assured Assertions

    String consumerKey = Auth.CONSUMER_KEY;
    String consumerSecret = Auth.CONSUMER_SECRET;
    String accessToken = Auth.ACCESS_TOKEN;
    String accessSecret = Auth.ACCESS_SECRET;


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.twitter.com";
        RestAssured.basePath = "/1.1/statuses";
    }

    // Hard Assertions

    @Test()
    public void testHardAssertions() {

        given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("user_id", "restAutomation")
                .when()
                .get("/user_timeline.json")
                .then()
                .log().body()
                .statusCode(200)
                .body("user.name", hasItem("Sinan Goktas"))
                .body("entities.hashtags[0].text", hasItem("multiple"))
                .body("entities.hashtags[0].size()", equalTo(2))
                .body("entities.hashtags[0]", hasSize(2))
                .body("entities.hashtags[1].size()", lessThan(2));


    }

    // Soft Assertions

    @Test()
    public void testSoftAssertions() {

        given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("user_id", "restAutomation")
                .when()
                .get("/user_timeline.json")
                .then()
                .log().body()
                .statusCode(200)
                .body("user.name", hasItem("Sinan Goktas"))
                .body("entities.hashtags[0].text", hasItem("multiple"))

                // Here we will do SOFT validation. If equalTo() fails it will still carry on
                // and run hasSize() and lessThan()
                .body("entities.hashtags[0].size()", equalTo(2),
                        "entities.hashtags[0]", hasSize(2),
                        "entities.hashtags[1].size()", lessThan(2));


    }

    // Root Path

    @Test()
    public void testRootPath() {

        RestAssured.rootPath = "entities.hashtags[0]";

        given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("user_id", "restAutomation")
                .when()
                .get("/user_timeline.json")
                .then()
                .log().body()
                .rootPath("user")
                .body("name", hasItem("Sinan Goktas"))
                .body("screen_name", hasItem("restAutomation"))
                .rootPath("entities.hashtags[0]")
                .body("text", hasItem("multiple"))

                // Here we will do SOFT validation. If equalTo() fails it will still carry on
                // and run hasSize() and lessThan()
                .body("size()", equalTo(2),
                        "entities.hashtags[0]", hasSize(2),
                        "size()", lessThan(2));


    }

    @Test()
    public void testResponseTime() {

        long expectedResponseTime = TimeUnit.MILLISECONDS.toMillis(100);
        long responseTime =

                given()
                        .auth()
                        .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                        .queryParam("user_id", "restAutomation")
                        .when()
                        .get("/user_timeline.json")
                        .timeIn(TimeUnit.SECONDS);


        assert responseTime < expectedResponseTime;

    }
}
