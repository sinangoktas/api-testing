package twitter.com.statuses;

import twitter.com.common.RestUtilities;
import twitter.com.constants.EndPoints;
import twitter.com.constants.Path;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;

import java.util.ArrayList;

public class UserTimelineTest {

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void setup() {
        requestSpec = RestUtilities.getRequestSpec();
        requestSpec.basePath(Path.STATUSES);
        requestSpec.queryParam("user_id", "restAutomation");
        responseSpec = RestUtilities.getResponseSpec();


    }

    @Test
    public void readTweets1() {

        given()
                .spec(RestUtilities.createQueryParam(requestSpec, "count", "1"))
                .when()
                .get(EndPoints.STATUSES_USER_TIMELINE)
                .then()
                .log().all()
                .spec(responseSpec)
                .body("user.screen_name", hasItem("restAutomation"));

    }

    @Test
    public void readTweets2() {

        Response response = RestUtilities.getResponse(
                RestUtilities.createQueryParam(requestSpec, "count", "1"), "get");

        ArrayList<String> screenNameList = response.path("user.screen_name");
        Assert.assertTrue(screenNameList.contains("restAutomation"));

    }
}
