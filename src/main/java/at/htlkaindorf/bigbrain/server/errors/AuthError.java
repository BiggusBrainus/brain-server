package at.htlkaindorf.bigbrain.server.errors;

public class AuthError extends Exception {
    public AuthError(String message) {
        super(message);
    }
}
