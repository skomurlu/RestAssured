package Campus;

import Campus.Model.DocumentTypes;
import Campus.Model.HrPositionCategory;
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

public class DocumentTypesTest {

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

    String name=getRandom(4);
    //String stage=getRandom(2);
    String getStage="STUDENT_REGISTRATION";
    String DocumentTypesID;

    @Test
    public void createDocumentTypes()
    {

        DocumentTypes documentTypes=new DocumentTypes(name, getStage);

        DocumentTypesID= given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(documentTypes)

                .when()
                .post("school-service/api/attachments")

                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(name))
                .extract().jsonPath().getString("id")
        ;

        System.out.println("name manual= " + name);

    }

}
