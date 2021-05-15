package ergastF1;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ErgastF1Examples1 {

    @BeforeClass
    public void initPath() {

        RestAssured.baseURI = "http://ergast.com";
    }

    @Test
    public void checkResponseCodeForCorrectRequest() {

        given().
                when().
                get("/api/f1/2016/drivers.json").
                then().
                assertThat().
                statusCode(200);
    }

    @Test
    public void checkResponseCodeForIncorrectRequest() {

        given().
                when().
                get("/api/f1/incorrect.json").
                then().
                assertThat().
                statusCode(400);
    }

    @Test
    public void checkResponseContentTypeJson() {

        given().
                when().
                get("/api/f1/2016/drivers.json").
                then().
                assertThat().
                contentType("application/json");
    }

    @Test
    public void checkTheFirstRaceOf2014WasAtAlbertPark() {

        given().
                when().
                get("/api/f1/2014/1/circuits.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.circuitId[0]", equalTo("albert_park"));
    }

    @Test
    public void checkThereWasARaceAtSilverstoneIn2014() {

        given().
                when().
                get("/api/f1/2014/circuits.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.circuitId", hasItem("silverstone"));
    }


    @Test
    public void checkThereWasNoRaceAtNurburgringIn2014() {

        given().
                when().
                get("/api/f1/2014/circuits.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.circuitId", not(hasItem("nurburgring")));
    }

}
