package at.htlkaindorf.bigbrain.server.errors;

/**
 * A more general error that can be thrown
 * whenever there's a problem with authentication,
 * such as: unknown username, wrong password, etc.
 * @version BigBrain v1
 * @since 12.05.2021
 * @author m4ttm00ny
 */

public class AuthError extends Exception {
    public AuthError(String message) {
        super(message);
    }
}
