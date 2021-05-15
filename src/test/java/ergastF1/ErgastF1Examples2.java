package ergastF1;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class ErgastF1Examples2 {

    @BeforeClass
    public void initPath() {

        RestAssured.baseURI = "http://ergast.com";
    }

    /*******************************************************
     * Create a DataProvider that specifies in which country
     * a specific circuit can be found (specify that Monza
     * is in Italy, for example)
     ******************************************************/

    @DataProvider(name = "circuitsNameAndCountry")
    public Object[][] createCircuitData() {
        return new String[][] {
                { "monza", "Italy" },
                { "spa", "Belgium" },
                { "sepang", "Malaysia" }
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
        return new Object[][] {
                { "1", 1 },
                { "2", 3 },
                { "3", 2 },
                { "4", 2 }
        };
    }


    @Test(dataProvider = "circuitsNameAndCountry")
    public void checkCountryForCircuit(String circuitName, String circuitCountry) {

        given().
                pathParam("circuitName", circuitName).
                when().
                get("/api/f1/circuits/{circuitName}.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.Location[0].country",equalTo(circuitCountry));
    }


    @Test(dataProvider = "pitStops")
    public void checkNumberOfPitstopsForMaxVerstappenIn2015(String raceNumber, int numberOfPitstops) {

        given().
                pathParam("raceNumber", raceNumber).
                when().
                get("/api/f1/2015/{raceNumber}/drivers/max_verstappen/pitstops.json").
                then().
                assertThat().
                body("MRData.RaceTable.Races[0].PitStops",hasSize(numberOfPitstops));
    }




}
