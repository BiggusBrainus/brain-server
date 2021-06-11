package at.htlkaindorf.bigbrain.server.errors;

/**
 * This error should be thrown, when a
 * player tries to join a lobby that's already
 * in-game.
 * @version BigBrain v1
 * @since 25.05.2021
 * @author m4ttm00ny
 */

public class AlreadyInGameError extends Exception {
    public AlreadyInGameError(String message) {
        super(message);
    }
}
