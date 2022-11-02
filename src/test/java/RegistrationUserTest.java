import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RegistrationUserTest {

    private UserClient userClient;
    private User user;
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (user != null) {
            userClient.deleteUser(user);
            System.out.println("Конец теста");
        }
    }

    @Test
    @DisplayName("Авторизация с валидными параметрами")
    public void registerUserWhenCorrectParametersTest() {
        user = User.getRandom();
        Response response = userClient.create(user);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя с существующим email")
    public void registerUserWithExistingEmailTest() {
        user = User.getRandom();
        Response responseFirst = userClient.create(user);
        Response responseSecond = userClient.create(user);
        responseSecond.then().assertThat().statusCode(403)
                .and()
                .body("message", Matchers.equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void registerUserWithoutNameTest() {
        Response response = userClient.createUserWithoutName();
        response.then().assertThat().statusCode(403)
                .and()
                .body("success", Matchers.equalTo(false));
        if (response.statusCode() == 200) {
            userClient.deleteAccessToken(accessToken);
        }
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void registerUserWithoutLoginTest() {
        Response response = userClient.createUserWithoutEmail();
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
        if (response.statusCode() == 200) {
            userClient.deleteAccessToken(accessToken);
        }

    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void registerUserWithoutPasswordTest() {
        Response response = userClient.createUserWithoutPassword();
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
        if (response.statusCode() == 200) {
            userClient.deleteAccessToken(accessToken);
        }
    }

}