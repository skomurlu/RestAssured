package Campus;

import Campus.Model.Citizenship;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CitizenshipTest {

    Cookies cookies;

    @BeforeClass
    public void loginCampus() {
        baseURI ="https://demo.mersys.io/";

        // {"username": "richfield.edu", "password": "Richfield2020!", "rememberMe": "true"}

        Map<String, String> credential= new HashMap<>();
        credential.put("username", "richfield.edu");
        credential.put("password", "Richfield2020!");
        credential.put("rememberMe", "true");

        cookies=
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)

                        .when()
                        .post("auth/login")

                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
        ;
        System.out.println("cookies = " + cookies);
    }

    public String getRandom(int value)
    {
        return RandomStringUtils.randomAlphabetic(value);
    }

    String name=getRandom(8);
    String shortName=getRandom(4);
    String citizenShipId;

    @Test
    public void createCitizenship()
    {
        Citizenship citizenship=new Citizenship(name, shortName);

        citizenShipId= given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(citizenship)

                .when()
                .post("/school-service/api/citizenships")

                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(name))
                .extract().jsonPath().getString("id")
        ;
        System.out.println("name manual= " + name);
    }

    @Test (dependsOnMethods = "createCitizenship")
    public void createCitizenshipNegative()
    {
        Citizenship citizenship=new Citizenship(name, shortName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(citizenship)

                .when()
                .post("/school-service/api/citizenships")

                .then()
                .statusCode(400)
                .body("message", equalTo("The Citizenship with Name \""+ name + "\" already exists."))
        ;
        System.out.println("name manual= " + name);
    }

    @Test (dependsOnMethods = "createCitizenship")
    public void updateCitizenship(){

        String updateName=getRandom(8);
        Citizenship citizenship=new Citizenship(updateName, shortName);
        citizenship.setId(citizenShipId);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(citizenship)

                .when()
                .put("/school-service/api/citizenships")

                .then()
                .statusCode(200)
                .body("name", equalTo(updateName))
        ;
        System.out.println("updateName= " + updateName);
    }

    @Test (dependsOnMethods = "updateCitizenship")
    public void deleteById(){

        given()
                .cookies(cookies)
                .pathParam("citizenShipId", citizenShipId)

                .when()
                .delete("/school-service/api/citizenships/{citizenShipId}")

                .then()
                .statusCode(200)
                .log().body()
                ;
    }

    @Test (dependsOnMethods = "deleteById")
    public void deleteByIdNegative(){

        given()
                .cookies(cookies)
                .pathParam("citizenShipId", citizenShipId)

                .when()
                .delete("/school-service/api/citizenships/{citizenShipId}")

                .then()
                .statusCode(400)
                .log().body()
        ;
    }

}
