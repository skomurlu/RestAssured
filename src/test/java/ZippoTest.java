import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test() {
        given()
                    // hazırlık işlemlerini yapacağız.(token, body, parametreler)
                .when()
                    // linki ve metodu veriyoruz.

                .then()
                    // assertion ve verileri ele alma, extract
        ;
    }

    @Test
    public void statusCodeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()   // log.all() butun respons u gosterir
                .statusCode(200)    // status kontrolu
        ;
    }

    @Test
    public void contentTypeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()   // log.all() butun response u gosterir
                .statusCode(200)    // status kontrolu
                .contentType(ContentType.JSON) // hatali durum kontrolunu yapalim
        ;
    }

    @Test
    public void checkStateInResponseBody() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country", equalTo("United States")) // body.country == United States?
                .statusCode(200)
        ;
    }

//    body.country -> body("country",
//    body.'post code' ->body("post code"
//    body.'country abbreviation' -> body("country abbreviation"
//    body.places[0].'place name' -> body("body.places[0].'place name'"
//    body.places[0].state -> body("places[0].state"


    @Test
    public void bodyJsonPathTest2() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state", equalTo("California"))
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTest3() {
        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .body("places.'place name'", hasItem("Çaputçu Köyü")) // bir index verilmezse butun elemanları cagirir.
                .statusCode(200)
                // places.'place name'" bu bilgiler "Çaputçu Köyü" bu item a sahip mi?
        ;
    }

    @Test
    public void bodyArrayHasSizeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1)) // verilen path deki listin size kontrolu
                .statusCode(200)
        ;
    }

    @Test
    public void combiningTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1)) // verilen path deki listin size kontrolu
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .statusCode(200)
        ;
    }

    @Test
    public void pathParamTest() {
        given()
                .pathParam("Country", "us")
                .pathParam("ZipKod", 90210)
                .log().uri()

                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipKod}")

                .then()
                .log().body()

                .statusCode(200)
        ;
    }

    @Test
    public void pathParamTest2() {
        // 90210 dan 90250 kadar test sonuclarinda places in size nın hepsinde 1 geldigini test ediniz.

        for (int i=90210; i<=90213; i++) {
            given()
                    .pathParam("Country", "us")
                    .pathParam("ZipKod", i)
                    .log().uri()

                    .when()
                    .get("http://api.zippopotam.us/{Country}/{ZipKod}")

                    .then()
                    .log().body()
                    .body("places", hasSize(1))
                    .statusCode(200)
            ;
        }
    }

    @Test
    public void queryParamTest() {
                //https://gorest.co.in/public/v1/users?page=1
        given()
                .param("page", 1)
                .log().uri() // request linki

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .body("meta.pagination.page", equalTo(1))
                .statusCode(200)
        ;
    }

    @Test
    public void queryParamTest2() {
        //https://gorest.co.in/public/v1/users?page=1

        for (int pageNo = 1; pageNo <= 10; pageNo++) {
            given()
                    .param("page", pageNo)
                    .log().uri() // request linki

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(pageNo))
                    .statusCode(200)
            ;
        }
    }

    RequestSpecification requestSpecs;
    ResponseSpecification responseSpecs;

    @BeforeClass
    void Setup(){

        // RestAssured kendi statik degiskeni tanımlı deger atanıyor
        baseURI="https://gorest.co.in/public/v1";
        requestSpecs = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

        responseSpecs = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }


    @Test
    public void requestResponseSpecification() {
        //https://gorest.co.in/public/v1/users?page=1

        given()
                    .param("page", 1)
                    .spec(requestSpecs)

                    .when()
                    .get("/users") // url nin basinda gttp yoksa baseURI deki deger otomatik geliyor

                    .then()
                    .body("meta.pagination.page", equalTo(1))
                    .spec(responseSpecs)
            ;
    }


    // JSON extract
    @Test
    public void extractingTest() {

        String placeName= // parca aldık.
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .extract().path("places[0].'place name'")
                // extract metodu ile given ile baslayan satır, bir deger dondurur hale geldi, en sonda extract olmalı.
        ;

        System.out.println("placeName = " + placeName);
    }

    @Test
    public void extractingJsonPathInt() {

        int limit=
                given()

                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                //.log().body()
                .statusCode(200)
                .extract().path("meta.pagination.limit");
                  ;

        System.out.println("limit = " + limit);
        Assert.assertEquals(limit,10,"test sonucu");
    }

//https://jsonpathfinder.com/


    @Test
    public void extractingJsonPathInt2() {

        int id=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data[2].id");
        ;

        System.out.println("id = " + id);
    }


    @Test
    public void extractingJsonPathIntList() {

        List<Integer> idler=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.id"); // data daki butun id leri List seklinde verir
        ;

        System.out.println("idler = " + idler);
        Assert.assertTrue(idler.contains(3045));
    }


    @Test
    public void extractingJsonPathStringList() {

        List<String> isimler=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.name"); // data daki butun id leri List seklinde verir
        ;

        System.out.println("isimler = " + isimler);
        Assert.assertTrue(isimler.contains("Navin Patil"));
    }

    @Test
    public void extractingJsonPathResponseAll() {

       Response response=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response() // butun body alındı
        ;

        List<Integer> idler=response.path("data.id");
        List<String> isimler=response.path("data.name");
        int limit=response.path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        System.out.println("isimler = " + isimler);
        System.out.println("idler = " + idler);
    }


    @Test
    public void extractingJsonPOJO() { // POJO : Json Object i (Plain Old Java Object)
        Location yer=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .extract().as(Location.class); // Location şablonu
        ;

        System.out.println("yer = " + yer);

        System.out.println("yer.getCountry() = " + yer.getCountry());
        System.out.println("yer.getPlaces().get(0).getPlacename() = " +
                yer.getPlaces().get(0).getPlacename());
    }



}


/*
"post code": "90210",
    "country": "United States",
    "country abbreviation": "US",

    "places": [
        {
            "place name": "Beverly Hills",
            "longitude": "-118.4065",
            "state": "California",
            "state abbreviation": "CA",
            "latitude": "34.0901"
        }
    ]

   class Location{
       String postcode;
       String country;
       String countryabbreviation;
       ArrayList<Place> places
   }

   class Place{
       String placename;
       String longitude;
       String state;
       String stateabbreviation
       String latitude;
   }
 */