import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import params.CourierCreate;

@RunWith(Parameterized.class)
public class TestCheckOrder {
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
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
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

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void testOrderList(){
        Response response = orderList();
        checkResponse(response);
    }
}
