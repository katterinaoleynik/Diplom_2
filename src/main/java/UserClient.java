import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {

    private static final String USER_PATH = "api/auth";

    @Step("Создание пользователя")
    public Response create(User user) {
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/register");
        printResponseBodyToConsole(response, "запрос регистрации");
        return response;
    }

    @Step("Создание пользователя без имени")
    public Response createUserWithoutName() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email", email);
        loginReqObj.put("password", password);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/register");
        printResponseBodyToConsole(response, "запрос регистрации");
        return response;
    }

    @Step("Создание пользователя без email")
    public Response createUserWithoutEmail() {
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("password", password);
        loginReqObj.put("name", name);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/register");
        printResponseBodyToConsole(response, "запрос регистрации");
        return response;
    }

    @Step("Создание пользователя без пароля")
    public Response createUserWithoutPassword() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru";
        String name = RandomStringUtils.randomAlphabetic(10);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email", email);
        loginReqObj.put("name", name);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/register");
        printResponseBodyToConsole(response, "запрос регистрации");
        return response;
    }

    @Step("Авторизация под пользователем")
    public Response login(LoginRequestBody loginRequestBody) {
        Response response = given()
                .spec(getBaseSpec())
                .body(loginRequestBody)
                .when()
                .post(USER_PATH + "/login");
        printResponseBodyToConsole(response, "запрос логина");
        return response;
    }

    @Step("Авторизация под пользователем с неверным логином")
    public Response loginWithWrongLogin(User user) {

        String email = RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru";
        LoginRequestBody userBody = LoginRequestBody.from(user);
        String userPassword = userBody.password;
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email", email);
        loginReqObj.put("password", userPassword);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/login");
        printResponseBodyToConsole(response, "запрос логина");
        return response;
    }

    @Step("Авторизация под пользователем с неверным паролем")
    public Response loginWithWrongPassword(User user) {

        String userPassword = "abc";
        LoginRequestBody userBody = LoginRequestBody.from(user);
        String email = userBody.getEmail();
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email", email);
        loginReqObj.put("password", userPassword);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/login");
        printResponseBodyToConsole(response, "запрос логина");
        return response;
    }

    @Step("Удаление пользователя")
    public Response deleteUser(User user) {

        Response responseLogin = login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        Response response = given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(USER_PATH + "/user");
        printResponseBodyToConsole(response, "запрос на удаление пользователя");
        return response;
    }

    @Step("Выход из системы")
    public Response logOut(String refreshToken) {
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("token", refreshToken);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/logout");
        printResponseBodyToConsole(response, "запрос на выход из системы");
        return response;
    }

    @Step("Удаление пользователя")
    public Response deleteAccessToken(String accessToken) {
        Response response = given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(USER_PATH + "/user");
        printResponseBodyToConsole(response, "запрос на удаление пользователя");
        return response;
    }

    @Step("Изменение email пользователя без авторизации ")
    public Response changeUsersInfoEmail() {
        String userEmailSecond = (RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru").toLowerCase();
        System.out.println("новый email ===" + userEmailSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email", userEmailSecond);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response, "запрос на изменение email");
        return response;
    }

    @Step("Изменение пароля пользователя без авторизации")
    public Response changeUsersInfoPassword() {
        String userPasswordSecond = RandomStringUtils.randomAlphabetic(10);
        System.out.println("новый password === " + userPasswordSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("password", userPasswordSecond);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response, "запрос на изменение password");
        return response;
    }

    @Step("Изменение имени пользователя без авторизации")
    public Response changeUsersInfoName() {
        String userNameSecond = RandomStringUtils.randomAlphabetic(10);
        System.out.println("новый name === " + userNameSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("name", userNameSecond);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response, "запрос на изменение name");
        return response;
    }

    @Step("Изменение email пользователя с авторизацией")
    public Response changeUsersInfoEmail(String accessToken) {
        String userEmailSecond = (RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru").toLowerCase();
        System.out.println("новый email ===" + userEmailSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email", userEmailSecond);
        Response response = given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response, "запрос на изменение email");
        return response;
    }

    @Step("Изменение пароля пользователя с авторизацией")
    public Response changeUsersInfoPassword(String accessToken) {
        String userPasswordSecond = "test123";
        System.out.println("новый password === " + userPasswordSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("password", userPasswordSecond);
        Response response = given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response, "запрос на изменение password");
        return response;
    }

    @Step("Изменение имени пользователя с авторизацией")
    public Response changeUsersInfoName(String accessToken) {
        String userNameSecond = RandomStringUtils.randomAlphabetic(10);
        System.out.println("новый name === " + userNameSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("name", userNameSecond);
        Response response = given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response, "запрос на изменение name");
        return response;
    }

    @Step("Print response body to console")
    public void printResponseBodyToConsole(Response response, String requestName) {
        System.out.println("Ответ на " + requestName + response.body().asString());
    }
}