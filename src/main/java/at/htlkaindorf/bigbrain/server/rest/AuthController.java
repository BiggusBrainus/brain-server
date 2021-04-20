package at.htlkaindorf.bigbrain.server.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.crypto.SecretKey;

@RestController
public class AuthController {

    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @RequestMapping(value = "/api/auth", method = { RequestMethod.POST })
    public ResponseEntity<Object> auth() {
        String jwt = Jwts.builder().setSubject("test1234").signWith(KEY).compact();
        return new ResponseEntity<>(new String[]{jwt, new BCryptPasswordEncoder().encode("smolpp")}, HttpStatus.OK);
    }
}
