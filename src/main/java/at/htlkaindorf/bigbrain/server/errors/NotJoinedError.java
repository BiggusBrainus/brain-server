package at.htlkaindorf.bigbrain.server.errors;

/**
 * Should be thrown, whenever a player tries
 * to connect to a lobby, but they actually haven't
 * joined any.
 * @version BigBrain v1
 * @since 25.05.2021
 * @author m4ttm00ny
 */

public class NotJoinedError extends Exception {
    public NotJoinedError(String message) {
        super(message);
    }
}

