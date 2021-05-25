package at.htlkaindorf.bigbrain.server.errors;

public class InvalidSignatureError extends Exception {
    public InvalidSignatureError(String message) {
        super(message);
    }
}
