package courierCreate;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import params.CourierCreate;
import params.CourierLogin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class TestCreatePositive {
    private String login;
    private String password;
    private String firstName;

    public TestCreatePositive(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                //Перед каждой проверкой необходимо менять первый параметр на уникальный для каждого набора
                {"sprint71", "1234", "Vasya"},
                {"sprint72", "1234", ""}
        };
    }

    @Before
    public void baseurl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Передача данных регистрации")
    public Response signIn(){
        CourierCreate courier = new CourierCreate(login, password, firstName);
        Response response = given().header("Content-type", "application/json").and().body(courier).post("/api/v1/courier");
        return response;
    }

    @Step("Проверка поля ok")
    public void checkOk(Response response){
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Step("Проверка кода ответа")
    public void checkCode(Response response){
        response.then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("Создание пользователя: позитивные проверки")
    public void createPositive(){
        Response response = signIn();
        checkOk(response);
        checkCode(response);
    }

    @After
    public void deleteCourier(){
        CourierLogin courierLogin = new CourierLogin(login, password);
        int id = given().header("Content-type", "application/json").and().body(courierLogin).post("/api/v1/courier/login")
                .then().extract().body().path("id");
        given().delete("/api/v1/courier/{id}", id).then().assertThat().statusCode(200);
    }
}
