package tubes.models.execptions;

public class LoginFailedException extends Exception {
    public LoginFailedException(String message) {
        super("Login Failed: " + message);
    }
}
