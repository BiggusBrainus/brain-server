package at.htlkaindorf.bigbrain.server.rest;

import at.htlkaindorf.bigbrain.server.auth.Authenticator;
import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.beans.Lobby;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.db.access.QuestionsAccess;
import at.htlkaindorf.bigbrain.server.errors.LobbyExistsError;
import at.htlkaindorf.bigbrain.server.errors.UnknownUserException;
import at.htlkaindorf.bigbrain.server.game.GameManager;
import at.htlkaindorf.bigbrain.server.rest.req.JoinLobbyRequest;
import at.htlkaindorf.bigbrain.server.rest.req.LeaveLobbyRequest;
import at.htlkaindorf.bigbrain.server.rest.req.NewLobbyRequest;
import at.htlkaindorf.bigbrain.server.rest.res.GetLobbiesResponse;
import at.htlkaindorf.bigbrain.server.rest.res.JoinLobbyResponse;
import at.htlkaindorf.bigbrain.server.rest.res.LeaveLobbyResponse;
import at.htlkaindorf.bigbrain.server.rest.res.NewLobbyResponse;
import at.htlkaindorf.bigbrain.server.rest.res.errors.GetLobbiesError;
import at.htlkaindorf.bigbrain.server.rest.res.errors.JoinLobbyError;
import at.htlkaindorf.bigbrain.server.rest.res.errors.LeaveLobbyError;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            QuestionsAccess acc = QuestionsAccess.getInstance();
            GameManager.newLobby(req.getLobby().getName(), user, acc.getCategoriesById(req.getCategories()));
            return new ResponseEntity<>(new NewLobbyResponse(), HttpStatus.OK);
        } catch (UnknownUserException|SignatureException e) {
            return new ResponseEntity<>(new NewLobbyResponse(NewLobbyError.AUTH_FAILURE), HttpStatus.BAD_REQUEST);
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new NewLobbyResponse(NewLobbyError.OTHER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (LobbyExistsError lobbyExistsError) {
            return new ResponseEntity<>(new NewLobbyResponse(NewLobbyError.LOBBY_NAME_TAKEN), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetLobbiesResponse> get(@RequestParam(required = false) String cids) {
        List<Category> categories = new ArrayList<>();
        if (cids != null) {
            QuestionsAccess acc;
            try {
                acc = QuestionsAccess.getInstance();
                categories = acc.getCategoriesById(Arrays.stream(cids.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
            } catch (SQLException|ClassNotFoundException e) {
                return new ResponseEntity<>(new GetLobbiesResponse(GetLobbiesError.OTHER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        List<Lobby> lobbies = GameManager.getLobbiesByCategories(categories);
        return new ResponseEntity<>(new GetLobbiesResponse(lobbies), HttpStatus.OK);
    }

    @PostMapping(value = "/join", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<JoinLobbyResponse> join(@RequestBody JoinLobbyRequest req) {
        if (req.getLobby() == null || !GameManager.isLobby(req.getLobby())) {
            return new ResponseEntity<>(new JoinLobbyResponse(JoinLobbyError.MISSING_LOBBY), HttpStatus.BAD_REQUEST);
        }
        try {
            User user = Authenticator.getUser(req.getToken());
            GameManager.joinLobby(user, GameManager.getLobby(req.getLobby()));
            return new ResponseEntity<>(new JoinLobbyResponse(), HttpStatus.OK);
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new JoinLobbyResponse(JoinLobbyError.OTHER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnknownUserException e) {
            return new ResponseEntity<>(new JoinLobbyResponse(JoinLobbyError.AUTH_FAILURE), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/leave", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<LeaveLobbyResponse> leave(@RequestBody LeaveLobbyRequest req) {
        try {
            User user = Authenticator.getUser(req.getToken());
            GameManager.leaveLobby(user);
            return new ResponseEntity<>(new LeaveLobbyResponse(), HttpStatus.OK);
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new LeaveLobbyResponse(LeaveLobbyError.OTHER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnknownUserException e) {
            return new ResponseEntity<>(new LeaveLobbyResponse(LeaveLobbyError.AUTH_FAILURE), HttpStatus.BAD_REQUEST);
        }
    }
}
