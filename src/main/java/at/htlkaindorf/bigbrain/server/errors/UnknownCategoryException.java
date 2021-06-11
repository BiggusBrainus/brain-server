package at.htlkaindorf.bigbrain.server.errors;

/**
 * Should be thrown, whenever the category
 * that was asked for doesn't exist.
 * @version BigBrain v1
 * @since 14.04.2021
 * @author m4ttm00ny
 */

public class UnknownCategoryException extends Exception {
    public UnknownCategoryException(String message) {
        super(message);
    }
}
