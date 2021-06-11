package at.htlkaindorf.bigbrain.server.errors;

/**
 * Should be thrown, whenever a user tries
 * to create a lobby with a name that's already
 * in use.
 * @version BigBrain v1
 * @since 12.05.2021
 * @author m4ttm00ny
 */

public class LobbyExistsError extends Exception {
    public LobbyExistsError(String message) {
        super(message);
    }
}
