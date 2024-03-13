package ru.qa.scooter.praktikum.services.couriers.courierLogin;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.qa.scooter.praktikum.services.BaseTest;
import ru.qa.scooter.praktikum.services.params.CourierCreate;
import ru.qa.scooter.praktikum.services.params.CourierLogin;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestCourierLoginPositive extends BaseTest {
    @Before
    public void baseurl() {
        baseTestURL();
        createTestCourier("etalon", "1234","");
    }

    @Step("Отправка данных существующего пользователя")
    public Response login(){
        CourierLogin courier = new CourierLogin("etalon","1234");
        Response response = given().header("Content-type", "application/json").and().body(courier).post("/api/v1/courier/login");
        return response;
    }
    @Step("Проверка id")
    public void checkId(Response response){
        response.then().assertThat().body("id", notNullValue());
    }

    @Step("Проверка кода")
    public void checkCode(Response response){
        response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Логин: позитивные проверки")
    public void testLoginPositive(){
        Response response = login();
        checkCode(response);
        checkId(response);
    }

    @After
    public void deleteCourier(){
        deleteTestCourier("etalon","1234");
    }
}
