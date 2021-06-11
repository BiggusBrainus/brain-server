package at.htlkaindorf.bigbrain.server.errors;

/**
 * Should be thrown, whenever there's a problem
 * with a JWT's signature.
 * @version BigBrain v1
 * @since 25.05.2021
 * @author m4ttm00ny
 */

public class InvalidSignatureError extends Exception {
    public InvalidSignatureError(String message) {
        super(message);
    }
}
