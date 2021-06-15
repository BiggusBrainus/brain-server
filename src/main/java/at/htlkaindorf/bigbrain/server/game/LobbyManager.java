package at.htlkaindorf.bigbrain.server.game;

import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.beans.Lobby;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.errors.AlreadyInGameError;
import at.htlkaindorf.bigbrain.server.errors.LobbyExistsError;
import at.htlkaindorf.bigbrain.server.errors.NotJoinedError;
import at.htlkaindorf.bigbrain.server.errors.UnknownCategoryException;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandler;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import at.htlkaindorf.bigbrain.server.websockets.res.LobbyPlayersUpdateResponse;
import at.htlkaindorf.bigbrain.server.websockets.res.StartLobbyResponse;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class contains all current lobbies and
 * deals with the logic surrounding them.
 * @version BigBrain v1
 * @since 15.05.2021
 * @author m4ttm00ny
 */

@Data
public class LobbyManager {
    private static Map<String, Lobby> lobbyLookup = new HashMap<>();
    private static Map<User, Lobby> userLookup = new HashMap<>();

    /**
     * Checks, whether or not a lobby with the given
     * name exists.
     * @param name  The name to check for.
     * @return Whether or not such a lobby exists.
     */
    public static boolean isLobby(String name) {
        return lobbyLookup.containsKey(name);
    }

    /**
     * Gets the lobby with the given name.
     * @param name  The name of the lobby to get.
     * @return The lobby with the given name.
     */
    public static Lobby getLobby(String name) {
        return lobbyLookup.get(name);
    }

    /**
     * Gets and returns all currently existent lobbies.
     * @return The list of all current lobbies.
     */
    public static List<Lobby> getLobbies() {
        return lobbyLookup.values().stream().filter(Predicate.not(Lobby::isHidden)).collect(Collectors.toList());
    }

    /**
     * Gets a list of all currently existent lobbies
     * with any of the given categories.
     * @param categories    The list of categories to search for.
     * @return The list of all lobbies with any of those categories.
     */
    public static List<Lobby> getLobbiesByCategories(List<Category> categories) {
        return categories != null && categories.size() > 0 ? lobbyLookup.values().stream().filter(l -> !l.isHidden() && l.getCategories().stream().anyMatch(categories::contains)).collect(Collectors.toList()) : getLobbies();
    }

    /**
     * Makes a player out of a user - adds a reference
     * to the lobby they're currently in, if there is any.
     * @param user      The user that should be turned into a player.
     */
    public static void makePlayer(User user) {
        if (userLookup.containsKey(user)) {
            user.setLobby(userLookup.get(user));
        }
    }

    /**
     * Creates a new public lobby - with the given name,
     * user and categories.
     * @param name          The name of the new lobby.
     * @param user          The user that created the lobby and should be put into it.
     * @param categories    The categories the lobby will use questions from.
     * @throws LobbyExistsError     Thrown, if a lobby with the given name already exists.
     */
    public static void newLobby(String name, User user, List<Category> categories) throws LobbyExistsError {
        newLobby(name, user, categories, false);
    }

    /**
     * Create new lobby - with the given name, user,
     * categories and visibility.
     * @param name          The name of the new lobby.
     * @param user          The user that created the lobby and should be put into it.
     * @param categories    The categories the lobby will use questions from.
     * @param hidden        Whether or not the lobby should be hidden from users.
     * @throws LobbyExistsError     Thrown, if a lobby with the given name already exists.
     */
    public static void newLobby(String name, User user, List<Category> categories, boolean hidden) throws LobbyExistsError {
        if (isLobby(name)) {
            throw new LobbyExistsError("This lobby name is already taken!");
        }
        leaveLobby(user);
        Lobby lobby = new Lobby(name, new ArrayList<>(){{add(user);}}, categories, hidden);
        lobbyLookup.put(name, lobby);
        userLookup.put(user, lobby);
    }

    /**
     * Put a given user into the given lobby.
     * @param user      The user that should be put into a lobby.
     * @param lobby     The lobby the user should be put into.
     * @throws AlreadyInGameError   The lobby is currently in-game; no user can join at the moment.
     */
    public static void joinLobby(User user, Lobby lobby) throws AlreadyInGameError {
        if (lobby.isInGame()) {
            throw new AlreadyInGameError("The game has already started!");
        }
        leaveLobby(user);
        lobby.getPlayers().add(user);
        userLookup.put(user, lobby);
        lobby.broadcast(new LobbyPlayersUpdateResponse(lobby.getPlayers()));
    }

    /**
     * Connects a user's websocket to the lobby. Otherwise,
     * no broadcasts can be send to all members of the lobby.
     * @param user      The user that the WebSocket session belongs to.
     * @param session   The user's WebSocket session.
     * @throws NotJoinedError   The user is not part of any lobby - therefore they can't connect to any.
     */
    public static void connectToLobby(User user, WebSocketSession session) throws NotJoinedError {
        if (user.getLobby() == null || getLobby(user.getLobby().getName()) == null || !user.getLobby().getPlayers().contains(user)) {
            throw new NotJoinedError("Join a lobby first!");
        }
        Lobby lobby = user.getLobby();
        lobby.getConnections().put(user, session);
        lobby.broadcast(new LobbyPlayersUpdateResponse(lobby.getPlayers()));
    }

    /**
     * Starts the game in the given lobby.
     * @param lobby     The lobby to start the game in.
     * @throws UnknownCategoryException     One of the lobby's categories wasn't found in the DB.
     * @throws SQLException                 One of the SQL queries along the way failed.
     * @throws ClassNotFoundException       The Postgres driver class couldn't be loaded.
     */
    public static void startLobby(Lobby lobby) throws UnknownCategoryException, SQLException, ClassNotFoundException {
        // Don't start the lobby, if it's already in game ...
        if (!lobby.isInGame()) {
            lobby.setGame(new Game(lobby));
            lobby.broadcast(new StartLobbyResponse());
            lobby.getGame().startGame();
        }
    }

    /**
     * Removes the given user from the lobby they're
     * currently in (if they're in any).
     * @param user  The user to remove from their lobby.
     */
    public static void leaveLobby(User user) {
        if (userLookup.containsKey(user)) {
            Lobby lobby = userLookup.get(user);
            lobby.getPlayers().remove(user);
            lobby.getConnections().remove(user);
            if (lobby.getPlayers().isEmpty()) {
                lobbyLookup.remove(lobby.getName());
            } else {
                lobby.broadcast(new LobbyPlayersUpdateResponse(lobby.getPlayers()));
            }
        }
        userLookup.remove(user);
    }
}
