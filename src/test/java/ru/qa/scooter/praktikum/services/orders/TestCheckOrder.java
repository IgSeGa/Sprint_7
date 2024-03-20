package ru.qa.scooter.praktikum.services.orders;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.qa.scooter.praktikum.services.BaseTest;

@RunWith(Parameterized.class)
public class TestCheckOrder extends BaseTest {
    private String hand;

    public TestCheckOrder(String hand){
        this.hand = hand;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"/api/v1/orders"},
                {"/api/v1/orders?courierId=269509"},
                {"/api/v1/orders?limit=10&page=0"},
                {"/api/v1/orders?limit=10&page=0&nearestStation=[\"110\"]"}
        };
    }
    @Before
    public void baseurl() {
        baseTestURL();
    }

    @Step("Отправка запроса на список заказов")
    public Response orderList(){
        Response response = given().get(hand);
        return response;
    }

    @Step("Проверка наличия данных в поле order")
    public void checkResponse(Response response){
        response.then().assertThat().body("orders",notNullValue());
    }

    @Step("Проверка статус-кода")
    public void checkCode(Response response){
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void testOrderList(){
        Response response = orderList();
        checkCode(response);
        checkResponse(response);
    }
}
