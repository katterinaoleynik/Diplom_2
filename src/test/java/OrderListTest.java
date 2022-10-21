import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderListTest {

    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = User.getRandom();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        userClient.deleteAccessToken(accessToken);
        System.out.println("Конец теста");
    }

    @Test
    @DisplayName("Получение списка заказов c валидными параметрами с авторизацией")
    public void getOrderListWithAccessTokenTest() {
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        accessToken = bearerAccessToken.substring(7);
        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa75",
                "61c0c5a71d1f82001bdaaa76", "61c0c5a71d1f82001bdaaa6d"});
        orderClient.create(order, accessToken);
        orderClient.create(order, accessToken);
        Response responseOrderList = orderClient.getOrderList(accessToken);
        responseOrderList.then().assertThat().statusCode(200)
                .and()
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Получение списка заказов c валидными параметрами без авторизации")
    public void getOrderListWithoutAccessTokenTest() {
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        accessToken = bearerAccessToken.substring(7);
        String refreshToken = responseLogin.jsonPath().getString("refreshToken");
        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa73",
                "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa6c"});
        orderClient.create(order, accessToken);
        orderClient.create(order, accessToken);
        userClient.logOut(refreshToken);
        Response responseOrderList = orderClient.getOrderList();
        responseOrderList.then().assertThat().statusCode(401)
                .and()
                .body("success", Matchers.equalTo(false));
    }
}