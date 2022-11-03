import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class CreateOrderTest {

    private OrderClient orderClient;
    private UserClient userClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Создание заказа с валидными параметрами без авторизации")
    public void createOrderWithoutAccessTokenTest() {
        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa73",
                "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa75"});
        Response response = orderClient.create(order);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа c некорректными параметрами без авторизации")
    public void createOrderWithIncorrectIngredientsAndWithoutAccessTokenTest() {
        Order order = new Order(new String[]{"test", "test", "test"});
        Response response = orderClient.create(order);
        response.then().assertThat().statusCode(500);

    }

    @Test
    @DisplayName("Создание заказа c пустыми параметрами без авторизации")
    public void createOrderWithoutIngredientsAndWithoutAccessTokenTest() {
        Order order = new Order(new String[]{});
        Response response = orderClient.create(order);
        response.then().assertThat().statusCode(400)
                .and()
                .body("success", Matchers.equalTo(false));
    }

    @Test
    @DisplayName("Заказ c валидными параметрами и авторизацией")
    public void createOrderWithAccessTokenTest() {
        User user = User.getRandom();
        userClient.create(user);
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa72",
                "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa6c"});
        Response response = orderClient.create(order, accessToken);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", Matchers.equalTo(true));

        userClient.deleteAccessToken(accessToken);
    }

    @Test
    @DisplayName("Заказ c невалидными параметрами и авторизацией")
    public void createOrderWithIncorrectIngredientsWithAccessTokenTest() {
        User user = User.getRandom();
        userClient.create(user);
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        Order order = new Order(new String[]{"test", "test", "test"});
        Response response = orderClient.create(order, accessToken);
        response.then().assertThat().statusCode(500);
        userClient.deleteAccessToken(accessToken);
    }

    @Test
    @DisplayName("Заказ c пустыми параметрами и авторизацией")
    public void createOrderWithoutIngredientsWithAccessTokenTest() {
        User user = User.getRandom();
        userClient.create(user);
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        Order order = new Order(new String[]{});
        Response response = orderClient.create(order, accessToken);
        response.then().assertThat().statusCode(400)
                .and()
                .body("success", Matchers.equalTo(false));
        userClient.deleteAccessToken(accessToken);

    }
}