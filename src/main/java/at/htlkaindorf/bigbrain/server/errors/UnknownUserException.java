package at.htlkaindorf.bigbrain.server.errors;

/**
 * Should be thrown, whenever the user that
 * was asked for doesn't exist.
 * @version BigBrain v1
 * @since 13.04.2021
 * @author m4ttm00ny
 */

public class UnknownUserException extends Exception {
    public UnknownUserException(String message) {
        super(message);
    }
}
