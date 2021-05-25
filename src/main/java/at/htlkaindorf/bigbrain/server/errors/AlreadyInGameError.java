package at.htlkaindorf.bigbrain.server.errors;

public class AlreadyInGameError extends Exception {
    public AlreadyInGameError(String message) {
        super(message);
    }
}
