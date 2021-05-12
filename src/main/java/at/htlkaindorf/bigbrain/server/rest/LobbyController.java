package at.htlkaindorf.bigbrain.server.rest;

import at.htlkaindorf.bigbrain.server.auth.Authenticator;
import at.htlkaindorf.bigbrain.server.beans.Lobby;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.errors.LobbyExistsError;
import at.htlkaindorf.bigbrain.server.errors.UnknownUserException;
import at.htlkaindorf.bigbrain.server.game.Game;
import at.htlkaindorf.bigbrain.server.rest.req.GetLobbiesRequest;
import at.htlkaindorf.bigbrain.server.rest.req.NewLobbyRequest;
import at.htlkaindorf.bigbrain.server.rest.res.GetLobbiesResponse;
import at.htlkaindorf.bigbrain.server.rest.res.NewLobbyResponse;
import at.htlkaindorf.bigbrain.server.rest.res.errors.NewLobbyError;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/lobbies")
public class LobbyController {

    @PostMapping(value = "/create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<NewLobbyResponse> create(@RequestBody NewLobbyRequest req) {
        if (req.getCategories() == null || req.getLobby() == null || req.getLobby().getName() == null || req.getLobby().getName().trim().equals("")) {
            return new ResponseEntity<>(new NewLobbyResponse(NewLobbyError.MISSING_LOBBY_NAME), HttpStatus.BAD_REQUEST);
        }
        try {
            User user = Authenticator.getUser(req.getToken());
            Game.newLobby(req.getLobby().getName(), user, req.getCategories());
            return new ResponseEntity<>(new NewLobbyResponse(), HttpStatus.OK);
        } catch (UnknownUserException|SignatureException e) {
            return new ResponseEntity<>(new NewLobbyResponse(NewLobbyError.AUTH_FAILURE), HttpStatus.BAD_REQUEST);
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new NewLobbyResponse(NewLobbyError.OTHER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (LobbyExistsError lobbyExistsError) {
            return new ResponseEntity<>(new NewLobbyResponse(NewLobbyError.LOBBY_NAME_TAKEN), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/get")
    public ResponseEntity<GetLobbiesResponse> get(@RequestBody GetLobbiesRequest req) {
        List<Lobby> lobbies = Game.getLobbiesByCategories(req.getCategories());
        return new ResponseEntity<>(new GetLobbiesResponse(true, lobbies), HttpStatus.OK);
    }
}
