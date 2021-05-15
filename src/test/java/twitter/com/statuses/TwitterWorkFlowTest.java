package twitter.com.statuses;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import twitter.com.common.RestUtilities;
import twitter.com.constants.EndPoints;
import twitter.com.constants.Path;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.given;

public class TwitterWorkFlowTest {

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    String tweetId = "";


    @BeforeClass
    public void setup() {

        requestSpec = RestUtilities.getRequestSpec();
        requestSpec.basePath(Path.STATUSES);
        responseSpec = RestUtilities.getResponseSpec();


    }

    @Test
    public void postTweet() {

        Response response = given()
                .spec(RestUtilities.createQueryParam(requestSpec, "status", "Hello World 7"))
                .when()
                .post(EndPoints.STATUSES_TWEET_POST)
                .then()
                .spec(responseSpec)
                .extract()
                .response();

        JsonPath jsonPath = RestUtilities.getJsonPath(response);
        tweetId = jsonPath.get("id_str");
        System.out.println("The tweet id is: " + tweetId);


    }

    @Test(dependsOnMethods = {"postTweet"})
    public void readTweet() {

        RestUtilities.setEndPoint(EndPoints.STATUSES_TWEET_READ_SINGLE);
        Response response = RestUtilities.getResponse(RestUtilities.createQueryParam(requestSpec, "id", tweetId), "get");

        String text = response.path("text");
        System.out.println("The tweet text is: " + text);


    }

    @Test(dependsOnMethods = {"readTweet"})
    public void deleteTweet() {

        given()
                .spec(RestUtilities.createPathParam(requestSpec, "id", tweetId))
                .when()
                .post(EndPoints.STATUSES_TWEET_DESTROY)
                .then()
                .spec(responseSpec);

    }
}
