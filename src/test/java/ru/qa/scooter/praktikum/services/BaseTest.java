package ru.qa.scooter.praktikum.services;
import io.restassured.RestAssured;
import ru.qa.scooter.praktikum.services.params.CourierCreate;
import ru.qa.scooter.praktikum.services.params.CourierLogin;
import static io.restassured.RestAssured.given;

public class BaseTest {
    public void baseTestURL(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public void createTestCourier(String login, String password, String firstName){
        CourierCreate courier = new CourierCreate(login, password, firstName);
        given().header("Content-type", "application/json").and().body(courier).post("/api/v1/courier");
    }
    public void deleteTestCourier(String login, String password){
        CourierLogin courierLogin = new CourierLogin(login, password);
        int id = given().header("Content-type", "application/json").and().body(courierLogin).post("/api/v1/courier/login")
                .then().extract().body().path("id");
        given().delete("/api/v1/courier/{id}", id).then().assertThat().statusCode(200);
    }
}
