import lombok.Getter;
import lombok.Setter;

public class LoginRequestBody {
    @Getter
    @Setter
    String email;
    String password;

    public LoginRequestBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static LoginRequestBody from(User user) {
        return new LoginRequestBody(user.getEmail(), user.password);
    }

    @Override
    public String toString() {
        return "loginRequestBody{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}