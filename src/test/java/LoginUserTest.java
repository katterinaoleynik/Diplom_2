import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginUserTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandom();
        Response responseCreate = userClient.create(user);
        String bearerAccessToken = responseCreate.jsonPath().getString("accessToken");
        accessToken = bearerAccessToken.substring(7);
    }

    @After
    public void tearDown() {
        userClient.deleteAccessToken(accessToken);
        System.out.println("Конец теста");
    }

    @Test
    @DisplayName("Авторизация с валидными параметрами")
    public void loginUserWhenCorrectParametersTest() {
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        responseLogin.then().assertThat().statusCode(200)
                .and()
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с невалидным логином")
    public void loginWithWrongLogin() {
        Response responseSecond = userClient.loginWithWrongLogin(user);
        responseSecond.then().assertThat().statusCode(401)
                .and()
                .body("message", Matchers.equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с невалидным паролем")
    public void loginWithWrongPassword() {
        Response responseSecond = userClient.loginWithWrongPassword(user);
        responseSecond.then().assertThat().statusCode(401)
                .and()
                .body("message", Matchers.equalTo("email or password are incorrect"));
    }
}
