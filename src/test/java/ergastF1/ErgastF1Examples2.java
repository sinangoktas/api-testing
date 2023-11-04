package ergastF1;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ErgastF1Examples2 {

    ResponseSpecification responseSpec;

    @BeforeClass
    public void initPath() {

        RestAssured.baseURI = "http://ergast.com";
    }

    @BeforeClass
    public void createResponseSpecification() {
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    /*******************************************************
     * Create a DataProvider that specifies in which season
     * a specific driver can be found (specify that alonso
     * is in 2014, for example)
     ******************************************************/

    @DataProvider(name = "seasonAndDrivers")
    public Object[][] createSeasonDriversData() {
        return new String[][]{
                {"2020", "aitken"},
                {"2015", "alonso"},
                {"2000", "alesi"}

        };
    }

    /*******************************************************
     * Create a DataProvider that specifies in which country
     * a specific circuit can be found (specify that Monza
     * is in Italy, for example)
     ******************************************************/

    @DataProvider(name = "circuitsNameAndCountry")
    public Object[][] createCircuitData() {
        return new String[][]{
                {"monza", "Italy"},
                {"spa", "Belgium"},
                {"sepang", "Malaysia"}
        };
    }


    /*******************************************************
     * Create a DataProvider that specifies for all races
     * (adding the first four suffices) in 2015 how many
     * pit stops Max Verstappen made
     * (race 1 = 1 pitstop, 2 = 3, 3 = 2, 4 = 2)
     ******************************************************/

    @DataProvider(name = "pitStops")
    public Object[][] createPitstopData() {
        return new Object[][]{
                {"1", 1},
                {"2", 3},
                {"3", 2},
                {"4", 2}
        };
    }

    @Test(dataProvider = "seasonAndDrivers")
    public void checkDriversForSeasons(String season, String driver) {

        given().
                pathParam("raceSeason", season).
                when().
                get("/api/f1/{raceSeason}/drivers.json").
                then().
                assertThat().
                body("MRData.DriverTable.Drivers[0].driverId", equalTo(driver));

    }


    @Test(dataProvider = "circuitsNameAndCountry")
    public void checkCountryForCircuit(String circuitName, String circuitCountry) {

        given().
                pathParam("circuitName", circuitName).
                when().
                get("/api/f1/circuits/{circuitName}.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.Location[0].country", equalTo(circuitCountry));
    }


    @Test(dataProvider = "pitStops")
    public void checkNumberOfPitstopsForMaxVerstappenIn2015(String raceNumber, int numberOfPitstops) {

        given().
                pathParam("raceNumber", raceNumber).
                when().
                get("/api/f1/2015/{raceNumber}/drivers/max_verstappen/pitstops.json").
                then().
                assertThat().
                body("MRData.RaceTable.Races[0].PitStops", hasSize(numberOfPitstops));
    }


}
