package ru.qa.scooter.praktikum.services.couriers.courierLogin;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.qa.scooter.praktikum.services.BaseTest;
import ru.qa.scooter.praktikum.services.params.CourierCreate;
import ru.qa.scooter.praktikum.services.params.CourierLogin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class TestCourierLoginNegative extends BaseTest {

    private String login;
    private String password;
    private String message;
    private int code;

    public TestCourierLoginNegative(String login, String password, String message, int code){
        this.login = login;
        this.password = password;
        this.message = message;
        this.code = code;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"etalon", "ЯНеПароль","Учетная запись не найдена", 404},
                {"МеняНеСуществует", "1234","Учетная запись не найдена", 404},
                {"", "1234","Недостаточно данных для входа", 400},
                {"etalon", "","Недостаточно данных для входа", 400},
                {"", "","Недостаточно данных для входа", 400}
        };
    }
    @Before
    public void baseurl() {
        baseTestURL();
        createTestCourier("etalon", "1234","");
    }

    @Step("Отправка некорректных данных логина")
    public Response login(){
        CourierLogin courier = new CourierLogin(login,password);
        Response response = given().header("Content-type", "application/json").and().body(courier).post("/api/v1/courier/login");
        return response;
    }

    @Step("Проверка текста сообщения")
    public void checkMessage(Response response){
        response.then().assertThat().body("message", equalTo(message));
    }
    @Step("Проверка кода ответа")
    public void checkCode(Response response){
        response.then().assertThat().statusCode(code);
    }

    @Test
    @DisplayName("Логин: негативные проверки")
    public void checkWrongLogin(){
        Response response = login();
        checkCode(response);
        checkMessage(response);
    }
    @After
    public void deleteCourier(){
        deleteTestCourier("etalon", "1234");
    }
}