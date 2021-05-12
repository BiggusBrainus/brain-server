package at.htlkaindorf.bigbrain.server.errors;

public class LobbyExistsError extends Exception {
    public LobbyExistsError(String message) {
        super(message);
    }
}
