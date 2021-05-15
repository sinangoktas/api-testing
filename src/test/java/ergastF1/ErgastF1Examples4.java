package ergastF1;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ErgastF1Examples4 {

    @BeforeClass
    public void initPath() {

        RestAssured.baseURI = "http://ergast.com";
    }


    /*******************************************************
     * Create a ResponseSpecification that checks whether:
     * - the response has statusCode 200
     * - the response contentType is JSON
     * - the value of MRData.CircuitTable.Circuits.circuitName[0]
     *   is equal to 'Albert Park Grand Prix Circuit'
     ******************************************************/

    ResponseSpecification respSpec;

    @BeforeClass
    public void createResponseSpecification() {

        respSpec = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                expectBody("MRData.CircuitTable.Circuits.circuitName[0]", equalTo("Albert Park Grand Prix Circuit")).
                build();
    }


    /*******************************************************
     * Retrieve the circuit data for the first race in 2014
     * Use the previously created ResponseSpecification to
     * execute the specified checks
     * Use /api/f1/2014/1/circuits.json
     * Additionally, check that the circuit is located in Melbourne
     ******************************************************/

    @Test
    public void useResponseSpecification() {

        given().
                when().
                get("/api/f1/2014/1/circuits.json").
                then().
                spec(respSpec).
                and().
                body("MRData.CircuitTable.Circuits.Location[0].locality",equalTo("Melbourne"));
    }

    /*
    ✅ What is RequestSpecification?

It used when a few common parameters are needed for multiple and/or different tests while creating a request.
import io.restassured.builder.RequestSpecBuilder;


✅ What is Response Specification?

This is used to validate a common response or a response needed for multiple tests from the body. We can also merge additional body expectations must all be fulfilled for the test to pass.
import io.restassured.builder.ResponseSpecBuilder;
     */


}
