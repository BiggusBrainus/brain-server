package at.htlkaindorf.bigbrain.server.rest;

import at.htlkaindorf.bigbrain.server.rest.req.LoginRequest;
import at.htlkaindorf.bigbrain.server.rest.req.RegisterRequest;
import at.htlkaindorf.bigbrain.server.rest.res.LoginResponse;
import at.htlkaindorf.bigbrain.server.rest.res.RegisterResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @PostMapping(value = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<LoginResponse> auth(@RequestBody LoginRequest req) {
        return new ResponseEntity<>(new LoginResponse(true, Jwts.builder().setSubject("admin").signWith(KEY).compact()), HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE } )
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req) {
        return new ResponseEntity<>(new RegisterResponse(true, new BCryptPasswordEncoder().encode(req.getPassword())), HttpStatus.OK);
    }
}
