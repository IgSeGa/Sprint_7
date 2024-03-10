package courierLogin;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import params.CourierCreate;
import params.CourierLogin;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestCourierLoginPositive {
    CourierLogin courier = new CourierLogin("etalon","1234");
    @Before
    public void baseurl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        CourierCreate courierCreate = new CourierCreate("etalon", "1234","");
        given().header("Content-type", "application/json").and().body(courierCreate).post("/api/v1/courier");
    }

    @Step("Отправка данных существующего пользователя")
    public Response login(){
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
        checkId(response);
        checkCode(response);
    }

    @After
    public void deleteCourier(){
        int id = given().header("Content-type", "application/json").and().body(courier).post("/api/v1/courier/login")
                .then().extract().body().path("id");
        given().delete("/api/v1/courier/{id}", id).then().assertThat().statusCode(200);
    }
}
