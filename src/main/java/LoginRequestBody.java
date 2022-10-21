public class LoginRequestBody {

    public final String email;
    public final String password;

    public LoginRequestBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static LoginRequestBody from(User user) {
        return new LoginRequestBody(user.email, user.password);
    }

    @Override
    public String toString() {
        return "loginRequestBody{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}