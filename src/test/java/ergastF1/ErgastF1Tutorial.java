package ergastF1;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

public class ErgastF1Tutorial {

    @Test
    public void test_NumberOfCircuitsFor2017Season_ShouldBe20() {

        given().
                when().
                get("http://ergast.com/api/f1/2017/circuits.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.circuitId", hasSize(20));
    }

    @Test
    public void test_ResponseHeaderData_ShouldBeCorrect() {

        given().
                when().
                get("http://ergast.com/api/f1/2017/circuits.json").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON).
                and().
                header("Content-Length", equalTo("4551"));
    }

    @Test
    public void test_Md5CheckSumForTest_ShouldBe098f6bcd4621d373cade4e832627b4f6() {

        String originalText = "test";
        String expectedMd5CheckSum = "098f6bcd4621d373cade4e832627b4f6";

        given().
                param("text", originalText).
                when().
                get("http://md5.jsontest.com").
                then().
                assertThat().
                body("md5", equalTo(expectedMd5CheckSum));
    }

    @Test
    public void test_NumberOfCircuits_ShouldBe20_Parameterized() {

        String season = "2017";
        int numberOfRaces = 20;

        given().
                pathParam("raceSeason", season).
                when().
                get("http://ergast.com/api/f1/{raceSeason}/circuits.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.circuitId", hasSize(numberOfRaces));
    }

    @DataProvider(name = "raceSeasonAndNumberOfRaces")
    public Object[][] createTestDataRecords() {
        return new Object[][]{
                {"2017", 20},
                {"2016", 21},
                {"1966", 9}
        };
    }

    @Test(dataProvider = "raceSeasonAndNumberOfRaces")
    public void test_NumberOfCircuits_ShouldBe_DataDriven(String raceSeason, int numberOfRaces) {

        given().
                pathParam("raceSeason", raceSeason).
                when().
                get("http://ergast.com/api/f1/{raceSeason}/circuits.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.circuitId", hasSize(numberOfRaces));
    }

    // if you are using username and password for authentication
    // auth().preemptive().basic(username, password)

    @Test
    public void test_APIWithBasicAuthentication_ShouldBeGivenAccess() {

        given().
                auth().
                preemptive().
                basic("username", "password").
                when().
                get("http://path.to/basic/secured/api").
                then().
                assertThat().
                statusCode(200);
    }

    // if you are using authentication token
    // auth().oauth2(authenticationToken)

    @Test
    public void test_APIWithOAuth2Authentication_ShouldBeGivenAccess() {

        given().
                auth().
                oauth2("YOUR_AUTHENTICATION_TOKEN_GOES_HERE").
                when().
                get("http://path.to/oath2/secured/api").
                then().
                assertThat().
                statusCode(200);
    }

    @Test
    public void test_ScenarioRetrieveFirstCircuitFor2017SeasonAndGetCountry_ShouldBeAustralia() {

        // First, retrieve the circuit ID for the first circuit of the 2017 season
        String circuitId = given().
                when().
                get("http://ergast.com/api/f1/2017/circuits.json").
                then().
                // extract from path
                extract().
                path("MRData.CircuitTable.Circuits.circuitId[0]");

        // Then, retrieve the information known for that circuit and verify it is located in Australia
        given().
                pathParam("circuitId", circuitId).
                when().
                get("http://ergast.com/api/f1/circuits/{circuitId}.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.Location[0].country", equalTo("Australia"));
    }

    // ResponseSpecs helpful if you have common validations among multiple test cases

    ResponseSpecification checkStatusCodeAndContentType =
            new ResponseSpecBuilder().
                    expectStatusCode(200).
                    expectContentType(ContentType.JSON).
                    build();


    @Test
    public void test_NumberOfCircuits_ShouldBe20_UsingResponseSpec() {

        given().
                when().
                get("http://ergast.com/api/f1/2017/circuits.json").
                then().
                assertThat().
                spec(checkStatusCodeAndContentType).
                and().
                body("MRData.CircuitTable.Circuits.circuitId", hasSize(20));
    }


    @Test
    public void useQueryParametersSingleTestcase() {

        given().
                queryParam("text", "testcaseOne").
                when().
                get("http://md5.jsontest.com").
                then().
                body("md5", equalTo("4ff1c9b1d1f23c6def53f957b1ed827f"));
    }


    @Test
    public void useSinglePathParameter() {

        given().
                pathParam("driverName", "max_verstappen").
                when().
                get("http://ergast.com/api/f1/drivers/{driverName}.json").
                then()
                .body("MRData.DriverTable.Drivers.permanentNumber[0]", equalTo("33"));
    }



    @Test(enabled = false)
    public void useMultipleQueryParameters() {

        given().
                params("q", "Kopenhagen", "mode", "xml").
                when().
                get("http://api.openweathermap.org/data/2.5/weather").
                then().
                body("current.city.country", equalTo("Denmark"));
    }

}
