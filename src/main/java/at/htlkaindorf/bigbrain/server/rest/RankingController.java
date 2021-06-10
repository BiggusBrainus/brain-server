package at.htlkaindorf.bigbrain.server.rest;

import at.htlkaindorf.bigbrain.server.beans.Rank;
import at.htlkaindorf.bigbrain.server.db.access.UsersAccess;
import at.htlkaindorf.bigbrain.server.rest.res.GetRankingResponse;
import at.htlkaindorf.bigbrain.server.rest.res.errors.GetRankingResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/ranking")
public class RankingController {

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetRankingResponse> get(@RequestParam(required = false) Integer n) {
        n = n != null ? n : 25;
        try {
            UsersAccess acc = UsersAccess.getInstance();
            return new ResponseEntity<>(new GetRankingResponse(acc.getTopN(n)), HttpStatus.OK);
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new GetRankingResponse(GetRankingResponseError.OTHER_ERROR), HttpStatus.OK);
        }
    }
}
