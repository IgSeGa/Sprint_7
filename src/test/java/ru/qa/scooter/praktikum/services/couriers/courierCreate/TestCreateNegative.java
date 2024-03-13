package ru.qa.scooter.praktikum.services.couriers.courierCreate;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.qa.scooter.praktikum.services.BaseTest;
import ru.qa.scooter.praktikum.services.params.CourierCreate;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class TestCreateNegative extends BaseTest {
    private String login;
    private String password;
    private String firstName;
    private String message;
    private int code;

    public TestCreateNegative(String login, String password, String firstName, String message, int code) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.message = message;
        this.code = code;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"etalon", "1234", "Vasya", "Этот логин уже используется. Попробуйте другой.", 409},
                {"", "1234", "Vasya", "Недостаточно данных для создания учетной записи", 400},
                {"etalon", "", "Vasya", "Недостаточно данных для создания учетной записи", 400},
                {"", "", "Vasya", "Недостаточно данных для создания учетной записи", 400}
        };
    }

    @Before
    public void setUP() {
        baseTestURL();
        createTestCourier("etalon", "1234", "Vasya");
    }
    @Step("Отправка запроса неполных и неверных данных регистрации")
    public Response signIn(){
        CourierCreate courier = new CourierCreate(login, password, firstName);
        Response response = given().header("Content-type", "application/json").and().body(courier).post("/api/v1/courier");
        return response;
    }
    @Step("Проверка сообщения об ошибке")
    public void checkMessage(Response response){
        response.then().assertThat().body("message", equalTo(message));
    }
    @Step("Проверка кода ошибки")
    public void checkCode(Response response){
        response.then().assertThat().statusCode(code);
    }

    @Test
    @DisplayName("Создание пользователя: негативные проверки")
    public void createNegative(){
        Response response = signIn();
        checkCode(response);
        checkMessage(response);
    }
    @After
    public void deleteCourier(){
        deleteTestCourier("etalon", "1234");
    }
}