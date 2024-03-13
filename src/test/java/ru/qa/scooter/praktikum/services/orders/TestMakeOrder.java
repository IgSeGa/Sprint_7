package ru.qa.scooter.praktikum.services.orders;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.qa.scooter.praktikum.services.params.MakeOrder;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestMakeOrder {
    Gson gson = new Gson();
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public TestMakeOrder(String firstName, String lastName, String address, int metroStation, String phone,
                         int rentTime,String deliveryDate, String comment, String[] color){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
    @Parameterized.Parameters
    public static Object[][] getData() {
        String[]black ={"BLACK"};
        String[]grey = {"GREY"};
        String[]both = {"BLACK", "GREY"};
        String[]none = {};
        return new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", black},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", grey},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", both},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", none}
        };
    }
    @Before
    public void baseurl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @Step("Отправка данных пользователя")
    public Response makeOrder(){
        MakeOrder makeOrder = new MakeOrder(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color);
        Response response = given().body(makeOrder).post("/api/v1/orders");
        return response;
    }
    @Step("Проверка наличия данных в поле track")
    public void checkTrack(Response response){
        response.then().assertThat().body("track", notNullValue());
    }

    @Step("Проверка кода ответа")
    public void checkCode(Response response){
        response.then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("Проверка создания заказа")
    public void testMakeOrder(){
        Response response = makeOrder();
        checkCode(response);
        checkTrack(response);
    }
}