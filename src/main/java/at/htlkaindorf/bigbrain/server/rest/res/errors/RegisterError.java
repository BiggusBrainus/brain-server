package at.htlkaindorf.bigbrain.server.rest.res.errors;

public enum RegisterError {
    USERNAME_TAKEN,
    EMAIL_TAKEN,
    PASSWORD_TOO_SHORT,
    INVALID_EMAIL,
    OTHER_ERROR
}
