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

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @PostMapping(value = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        try {
            try {
                return new ResponseEntity<>(new LoginResponse(true, Authenticator.login(req.getUsername(), req.getPassword())), HttpStatus.OK);
            } catch (UnknownUserException|AuthError e) {
                return new ResponseEntity<>(new LoginResponse(false, LoginError.UNKNOWN_CREDS), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new LoginResponse(false, LoginError.OTHER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE } )
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req) {
        try {
            UsersAccess acc = UsersAccess.getInstance();
            try {
                acc.getUserByName(req.getUsername());
                return new ResponseEntity<>(new RegisterResponse(false, RegisterError.USERNAME_TAKEN), HttpStatus.BAD_REQUEST);
            } catch (UnknownUserException e) {
                acc.createUser(new User(req.getUsername(), req.getEmail(), req.getPassword()));
                return new ResponseEntity<>(new RegisterResponse(true, Authenticator.getAuthToken(req.getUsername())), HttpStatus.OK);
            }
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new RegisterResponse(false, RegisterError.OTHER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/confirm", consumes = { MediaType.APPLICATION_JSON_VALUE } )
    public ResponseEntity<ConfirmResponse> confirm(@RequestBody ConfirmRequest req) {
        return new ResponseEntity<>(new ConfirmResponse(Authenticator.confirmIdentity(req.getUsername(), req.getToken())), HttpStatus.OK);
    }
}
