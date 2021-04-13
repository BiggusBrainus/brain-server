package at.htlkaindorf.bigbrain.server.errors;

/**
 *
 * @author m4ttm00ny
 */

public class UnknownUserException extends Exception {
    public UnknownUserException(String message) {
        super(message);
    }
}
