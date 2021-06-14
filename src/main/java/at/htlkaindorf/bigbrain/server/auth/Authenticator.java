package at.htlkaindorf.bigbrain.server.auth;

import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.db.access.UsersAccess;
import at.htlkaindorf.bigbrain.server.errors.AuthError;
import at.htlkaindorf.bigbrain.server.errors.InvalidSignatureError;
import at.htlkaindorf.bigbrain.server.errors.UnknownUserException;
import at.htlkaindorf.bigbrain.server.game.LobbyManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.sql.SQLException;

/**
 * Deals with user authentication - it holds 
 * the current secret used for JWT signing, 
 * confirms web tokens, gets a user from their
 * token, etc. 
 * @version BigBrain v1
 * @since 12.05.2021
 * @author m4ttm00ny
 */

public class Authenticator {
    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a new JWT for a user after
     * confirming their password.
     * @param username  The user's username.
     * @param password  The entered password that should be confirmed.
     * @return A new JWT for the user to authenticate themselves with.
     * @throws AuthError                The wrong password was passed.
     * @throws UnknownUserException     No user with the given username exists.
     * @throws SQLException             The SQL query retrieving the user failed.
     * @throws ClassNotFoundException   The Postgres driver class couldn't be loaded.
     */
    public static String login(String username, String password) throws AuthError, UnknownUserException, SQLException, ClassNotFoundException {
        UsersAccess acc = UsersAccess.getInstance();
        User u = acc.getUserByName(username);
        if (u.checkPassword(password)) {
            return getAuthToken(username);
        }
        throw new AuthError("Wrong credentials!");
    }

    /**
     * Generates a new JWT for a given username.
     * @param username  The user's username.
     * @return A new JWT for the user to authenticate themselves with.
     */
    public static String getAuthToken(String username) {
        return Jwts.builder().setSubject(username).signWith(KEY, SignatureAlgorithm.HS256).compact();
    }

    /**
     * Confirms, whether or not the given JWT is valid and
     * can be used to authenticate the user with the given username.
     * @param username  The user's username.
     * @param token     The token that should be tested.
     * @return Whether or not the token is valid.
     */
    public static boolean confirmIdentity(String username, String token) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return jws.getBody().getSubject().equals(username);
        } catch (SignatureException e) {
            return false;
        }
    }

    /**
     * Get the user that's authenticated by the given JWT.
     * @param token The JWT used for authentication.
     * @return The user that the token belongs to.
     * @throws SQLException             The SQL query retrieving the user failed.
     * @throws ClassNotFoundException   The Postgres driver class couldn't be loaded.
     * @throws UnknownUserException     The token belongs to a user that doesn't exist.
     * @throws InvalidSignatureError    The token's signature is invalid - perhaps forged.
     */
    public static User getUser(String token) throws SQLException, ClassNotFoundException, UnknownUserException, InvalidSignatureError {
        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new InvalidSignatureError("Signature doesn't work out ... ");
        }
        UsersAccess acc = UsersAccess.getInstance();
        User u = acc.getUserByName(jws.getBody().getSubject());;
        LobbyManager.makePlayer(u);
        return u;
    }
}
