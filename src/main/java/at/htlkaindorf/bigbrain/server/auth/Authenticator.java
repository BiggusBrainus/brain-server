package at.htlkaindorf.bigbrain.server.auth;

import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.db.access.UsersAccess;
import at.htlkaindorf.bigbrain.server.errors.AuthError;
import at.htlkaindorf.bigbrain.server.errors.InvalidSignatureError;
import at.htlkaindorf.bigbrain.server.errors.UnknownUserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.sql.SQLException;
import java.util.Base64;

public class Authenticator {
    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String login(String username, String password) throws AuthError, UnknownUserException, SQLException, ClassNotFoundException {
        UsersAccess acc = UsersAccess.getInstance();
        User u = acc.getUserByName(username);
        if (u.checkPassword(password)) {
            return getAuthToken(username);
        }
        throw new AuthError("Wrong credentials!");
    }

    public static String getAuthToken(String username) {
        return Jwts.builder().setSubject(username).signWith(KEY, SignatureAlgorithm.HS256).compact();
    }

    public static boolean confirmIdentity(String username, String token) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return jws.getBody().getSubject().equals(username);
        } catch (SignatureException e) {
            return false;
        }
    }

    public static User getUser(String token) throws SQLException, ClassNotFoundException, UnknownUserException, InvalidSignatureError {
        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new InvalidSignatureError("Signature doesn't work out ... ");
        }
        UsersAccess acc = UsersAccess.getInstance();
        return acc.getUserByName(jws.getBody().getSubject());
    }
}
