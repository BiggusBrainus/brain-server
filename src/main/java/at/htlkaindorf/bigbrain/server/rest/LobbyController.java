package at.htlkaindorf.bigbrain.server.rest;

import at.htlkaindorf.bigbrain.server.auth.Authenticator;
import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.beans.Lobby;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.db.access.QuestionsAccess;
import at.htlkaindorf.bigbrain.server.errors.*;
import at.htlkaindorf.bigbrain.server.game.LobbyManager;
import at.htlkaindorf.bigbrain.server.rest.req.JoinLobbyRequest;
import at.htlkaindorf.bigbrain.server.rest.req.LeaveLobbyRequest;
import at.htlkaindorf.bigbrain.server.rest.req.NewLobbyRequest;
import at.htlkaindorf.bigbrain.server.rest.req.StartLobbyRequest;
import at.htlkaindorf.bigbrain.server.rest.res.*;
import at.htlkaindorf.bigbrain.server.rest.res.errors.*;
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

/**
 * The REST Controller for the /lobbies
 * URL endpoints - like the name suggests,
 * it deals with lobby & few game actions.
 * @version BigBrain v1
 * @since 12.05.2021
 * @author m4ttm00ny
 */

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
            LobbyManager.newLobby(req.getLobby().getName(), user, acc.getCategoriesById(req.getCategories()), req.getLobby().isHidden());
            return new ResponseEntity<>(new NewLobbyResponse(), HttpStatus.OK);
        } catch (UnknownUserException|InvalidSignatureError e) {
            return new ResponseEntity<>(new NewLobbyResponse(NewLobbyError.AUTH_FAILURE), HttpStatus.OK);
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new NewLobbyResponse(NewLobbyError.OTHER_ERROR), HttpStatus.OK);
        } catch (LobbyExistsError lobbyExistsError) {
            return new ResponseEntity<>(new NewLobbyResponse(NewLobbyError.LOBBY_NAME_TAKEN), HttpStatus.OK);
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
                return new ResponseEntity<>(new GetLobbiesResponse(GetLobbiesError.OTHER_ERROR), HttpStatus.OK);
            }
        }
        List<Lobby> lobbies = LobbyManager.getLobbiesByCategories(categories);
        return new ResponseEntity<>(new GetLobbiesResponse(lobbies), HttpStatus.OK);
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetCategoriesResponse> categories() {
        try {
            QuestionsAccess acc = QuestionsAccess.getInstance();
            return new ResponseEntity<>(new GetCategoriesResponse(acc.getAllCategories()), HttpStatus.OK);
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new GetCategoriesResponse(GetCategoriesError.OTHER_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/join", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<JoinLobbyResponse> join(@RequestBody JoinLobbyRequest req) {
        if (req.getLobby() == null || !LobbyManager.isLobby(req.getLobby())) {
            return new ResponseEntity<>(new JoinLobbyResponse(JoinLobbyError.MISSING_LOBBY), HttpStatus.OK);
        }
        try {
            User user = Authenticator.getUser(req.getToken());
            LobbyManager.joinLobby(user, LobbyManager.getLobby(req.getLobby()));
            return new ResponseEntity<>(new JoinLobbyResponse(), HttpStatus.OK);
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new JoinLobbyResponse(JoinLobbyError.OTHER_ERROR), HttpStatus.OK);
        } catch (UnknownUserException|InvalidSignatureError e) {
            return new ResponseEntity<>(new JoinLobbyResponse(JoinLobbyError.AUTH_FAILURE), HttpStatus.OK);
        } catch (AlreadyInGameError alreadyInGameError) {
            return new ResponseEntity<>(new JoinLobbyResponse(JoinLobbyError.ALREADY_IN_GAME), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/start", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<StartLobbyResponse> start(@RequestBody StartLobbyRequest req) {
        try {
            User user = Authenticator.getUser(req.getToken());
            if (user.getLobby() != null) {
                LobbyManager.startLobby(user.getLobby());
                return new ResponseEntity<>(new StartLobbyResponse(), HttpStatus.OK);
            }
            return new ResponseEntity<>(new StartLobbyResponse(StartLobbyError.NOT_PART_OF_LOBBY), HttpStatus.OK);
        } catch (SQLException|ClassNotFoundException|UnknownCategoryException e) {
            return new ResponseEntity<>(new StartLobbyResponse(StartLobbyError.OTHER_ERROR), HttpStatus.OK);
        } catch (UnknownUserException|InvalidSignatureError e) {
            return new ResponseEntity<>(new StartLobbyResponse(StartLobbyError.AUTH_FAILURE), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/leave", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<LeaveLobbyResponse> leave(@RequestBody LeaveLobbyRequest req) {
        try {
            User user = Authenticator.getUser(req.getToken());
            LobbyManager.leaveLobby(user);
            return new ResponseEntity<>(new LeaveLobbyResponse(), HttpStatus.OK);
        } catch (SQLException|ClassNotFoundException e) {
            return new ResponseEntity<>(new LeaveLobbyResponse(LeaveLobbyError.OTHER_ERROR), HttpStatus.OK);
        } catch (UnknownUserException|InvalidSignatureError e) {
            return new ResponseEntity<>(new LeaveLobbyResponse(LeaveLobbyError.AUTH_FAILURE), HttpStatus.OK);
        }
    }
}
