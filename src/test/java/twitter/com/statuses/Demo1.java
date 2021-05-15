package twitter.com.statuses;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import twitter.com.constants.Auth;

import static io.restassured.RestAssured.given;

public class Demo1 {

     /*
    Create a twitter app and get your api credentials
     */

    String consumerKey = Auth.CONSUMER_KEY;
    String consumerSecret = Auth.CONSUMER_SECRET;
    String accessToken = Auth.ACCESS_TOKEN;
    String accessSecret = Auth.ACCESS_SECRET;
    String tweetId = "";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.twitter.com";
        RestAssured.basePath = "/1.1/statuses";
    }

    @Test
    public void statusCodeVerification() {

        given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("status", "Tweet X")
                .when()
                .post("/update.json")
                .then()
                .statusCode(200);

    }


    @Test
    public void extractResponse() {

        Response response = given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("status", "Tweet Y")
                .when()
                .post("/update.json")
                .then()
                .statusCode(200)
                .extract()
                .response();
        // extracting from json response-1
        String id = response.path("id_str");
        System.out.println("The id is " + id); // or assert the id


        // extracting from json response-2
        String responseString = response.asString();
        System.out.println(responseString);

        JsonPath jsonPath = new JsonPath(responseString);
        String name = jsonPath.get("user.name");
        System.out.println("The user name is " + name); // or assert the name


    }

    @Test
    public void postTweet() {

        Response response = given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("status", "Hello Tweet-2")
                .when()
                .post("/update.json")
                .then()
                .statusCode(200)
                .extract()
                .response();

        tweetId = response.path("id_str");
        System.out.println("The tweet id is " + tweetId); // or assert the id


    }

    // https://api.twitter.com/1.1/statuses/show.json?id=210462857140252672

    @Test(dependsOnMethods = {"postTweet"})
    public void readTweet() {

        Response response = given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("id", tweetId)
                .when()
                .get("/show.json")
                .then()
                .extract()
                .response();

        String text = response.path("text");
        System.out.println("The tweet text is " + text); // or assert the id


    }

    // https://api.twitter.com/1.1/statuses/destroy/240854986559455234.json


    @Test(dependsOnMethods = {"readTweet"})
    public void deleteTweet() {
        given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("id", tweetId)
                .when()
                .post("/destroy.json")
                .then()
                .statusCode(200);

    }

    // id is a path parameter in destroy end point unlike read end point
    // therefore we can use path parameter for destroying


    @Test(dependsOnMethods = {"readTweet"})
    public void deleteTweet_pathParam() {
        given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .pathParam("id", tweetId)
                .when()
                .post("/destroy/{id}.json")
                .then()
                .statusCode(200);

    }

    // Request Logging

    @Test
    public void testRequestLogging() {

        given()
                .log()
                //.headers()
                //.body()
                //.parameters()
                //.all()
                .ifValidationFails()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("status", "Hello REST")
                .when()
                .post("/update.json")
                .then()
                .statusCode(200);

    }


    // Response Logging

    @Test
    public void testResponseLogging() {

        given()
                .auth()
                .oauth(consumerKey, consumerSecret, accessToken, accessSecret)
                .queryParam("status", "Hello REST-2")
                .when()
                .post("/update.json")
                .then()
                .log()
                .ifError()
                .statusCode(200);

    }
}
