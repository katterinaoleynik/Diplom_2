import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class OrderClient extends RestAssuredClient {

    private static final String ORDER_PATH = "api/orders";

    @Step("Создание заказа без авторизации")
    public Response create(Order order) {
        Response response = RestAssured.given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH);
        printResponseBodyToConsole(response, "запрос на создание заказа неавторизованным пользователем");
        return response;
    }

    @Step("Создание заказа с авторизацией")
    public Response create(Order order, String accessToken) {
        Response response = RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(order)
                .when()
                .post(ORDER_PATH);
        printResponseBodyToConsole(response, "запрос на создание заказа авторизованным пользователем");
        return response;
    }

    @Step("Получение списка заказов с авторизацией")
    public Response getOrderList(String accessToken) {
        Response response = RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .get(ORDER_PATH);
        printResponseBodyToConsole(response, "запрос на получение списка заказов авторизованным пользователем");
        return response;
    }

    @Step("Получение списка заказов без авторизации")
    public Response getOrderList() {
        Response response = RestAssured.given()
                .spec(getBaseSpec())
                .get(ORDER_PATH);
        printResponseBodyToConsole(response, "запрос на получение списка заказов неавторизованным пользователем");
        return response;
    }

    @Step("Print response body to console")
    public void printResponseBodyToConsole(Response response, String requestName) {
        System.out.println("Ответ на " + requestName + response.body().asString());
    }
}
