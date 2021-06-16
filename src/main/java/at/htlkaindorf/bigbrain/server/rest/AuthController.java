package at.htlkaindorf.bigbrain.server.rest;

import at.htlkaindorf.bigbrain.server.auth.Authenticator;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.db.access.UsersAccess;
import at.htlkaindorf.bigbrain.server.errors.AuthError;
import at.htlkaindorf.bigbrain.server.errors.UnknownUserException;
import at.htlkaindorf.bigbrain.server.rest.req.ConfirmRequest;
import at.htlkaindorf.bigbrain.server.rest.req.LoginRequest;
import at.htlkaindorf.bigbrain.server.rest.req.RegisterRequest;
import at.htlkaindorf.bigbrain.server.rest.res.ConfirmResponse;
import at.htlkaindorf.bigbrain.server.rest.res.LoginResponse;
import at.htlkaindorf.bigbrain.server.rest.res.RegisterResponse;
import at.htlkaindorf.bigbrain.server.rest.res.errors.LoginError;
import at.htlkaindorf.bigbrain.server.rest.res.errors.RegisterError;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.sql.SQLException;

/**
 * The REST Controller for the /auth
 * URL endpoints - like the name suggests, 
 * it deals with user authentication.
 * @version BigBrain v1
 * @since 20.04.2021
 * @author m4ttm00ny
 */

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @PostMapping(value = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        System.out.printf("[INFO]: New Login for %s\n", req.getUsername());
        try {
            try {
                return new ResponseEntity<>(new LoginResponse(Authenticator.login(req.getUsername(), req.getPassword())), HttpStatus.OK);
            } catch (UnknownUserException|AuthError e) {
                return new ResponseEntity<>(new LoginResponse(LoginError.UNKNOWN_CREDS), HttpStatus.OK);
            }
        } catch (SQLException|ClassNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new LoginResponse(LoginError.OTHER_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE } )
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req) {
        System.out.printf("[INFO]: New User %s registered\n", req.getUsername());
        try {
            UsersAccess acc = UsersAccess.getInstance();
            try {
                acc.getUserByName(req.getUsername());
                return new ResponseEntity<>(new RegisterResponse(RegisterError.USERNAME_TAKEN), HttpStatus.OK);
            } catch (UnknownUserException e) {
                acc.createUser(new User(req.getUsername(), req.getEmail(), req.getPassword()));
                return new ResponseEntity<>(new RegisterResponse(Authenticator.getAuthToken(req.getUsername())), HttpStatus.OK);
            }
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new RegisterResponse(RegisterError.OTHER_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/confirm", consumes = { MediaType.APPLICATION_JSON_VALUE } )
    public ResponseEntity<ConfirmResponse> confirm(@RequestBody ConfirmRequest req) {
        return new ResponseEntity<>(new ConfirmResponse(Authenticator.confirmIdentity(req.getUsername(), req.getToken())), HttpStatus.OK);
    }
}
