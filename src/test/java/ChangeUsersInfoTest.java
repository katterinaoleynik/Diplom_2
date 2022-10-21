import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeUsersInfoTest {

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
    @DisplayName("Измененение email пользователя без токена")
    public void changeUserEmailWithoutAccessTokenTest() {
        Response responseChangeUserInfo = userClient.changeUsersInfoEmail();
        responseChangeUserInfo.then().assertThat().statusCode(401)
                .and()
                .body("success", Matchers.equalTo(false));
    }

    @Test
    @DisplayName("Измененение пароля пользователя без токена")
    public void changeUserPasswordWithoutAccessTokenTest() {
        Response responseChangeUserInfo = userClient.changeUsersInfoPassword();
        responseChangeUserInfo.then().assertThat().statusCode(401)
                .and()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Измененение имени пользователя без токена")
    public void changeUserNameWithoutAccessTokenTest() {
        Response responseChangeUserInfo = userClient.changeUsersInfoName();
        responseChangeUserInfo.then().assertThat().statusCode(401)
                .and()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Измененение email пользователея с токеном")
    public void changeUserEmailWithAccessTokenTest() {
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        Response responseChangeInfo = userClient.changeUsersInfoEmail(accessToken);
        responseChangeInfo.then().assertThat().statusCode(200)
                .and()
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Измененение пароля о пользователя с токеном")
    public void changeUserPasswordWithAccessTokenTest() {
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        Response responseChangeInfo = userClient.changeUsersInfoPassword(accessToken);
        responseChangeInfo.then().assertThat().statusCode(200)
                .and()
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Измененение имени о пользователя с токеном")
    public void changeUserNameWithAccessTokenTest() {
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        Response responseChangeInfo = userClient.changeUsersInfoName(accessToken);
        responseChangeInfo.then().assertThat().statusCode(200)
                .and()
                .body("success", Matchers.equalTo(true));
    }
}
