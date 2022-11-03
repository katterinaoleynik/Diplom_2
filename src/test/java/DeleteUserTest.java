import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeleteUserTest {

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
        System.out.println("Конец теста");
    }

    @Test
    @DisplayName("Удаление пользователя с валидными параметрами c авторизацией")
    public void deleteUserWhenCorrectParametersTest() {
        Response responseDelete = userClient.deleteUser(user);
        responseDelete.then().assertThat().statusCode(202)
                .and()
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Удаление пользователя с валидными параметрами через accessToken без авторизации")
    public void deleteUserWhenCorrectParametersWithAccessTokenTest() {
        Response responseDelete = userClient.deleteAccessToken(accessToken);
        responseDelete.then().assertThat().statusCode(202)
                .and()
                .body("success", Matchers.equalTo(true));
    }
}