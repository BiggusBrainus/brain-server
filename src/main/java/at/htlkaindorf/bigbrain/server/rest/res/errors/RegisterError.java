package at.htlkaindorf.bigbrain.server.rest.res.errors;

/**
 * @version BigBrain v1
 * @since 20.04.2021
 * @author m4ttm00ny
 */

public enum RegisterError {
    USERNAME_TAKEN,
    EMAIL_TAKEN,
    PASSWORD_TOO_SHORT,
    INVALID_EMAIL,
    OTHER_ERROR
}
