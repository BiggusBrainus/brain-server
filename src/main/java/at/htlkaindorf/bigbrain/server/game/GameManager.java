package at.htlkaindorf.bigbrain.server.game;

import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.beans.Lobby;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.errors.LobbyExistsError;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class GameManager {
    private static Map<String, Lobby> lobbyLookup = new HashMap<>();
    private static Map<User, Lobby> userLookup = new HashMap<>();

    public static boolean isLobby(String name) {
        return lobbyLookup.containsKey(name);
    }

    public static Lobby getLobby(String name) {
        return lobbyLookup.get(name);
    }

    public static List<Lobby> getLobbies() {
        return new ArrayList<>(lobbyLookup.values());
    }

    public static List<Lobby> getLobbiesByCategories(List<Category> categories) {
        return categories != null && categories.size() > 0 ? lobbyLookup.values().stream().filter(l -> l.getCategories().stream().anyMatch(categories::contains)).collect(Collectors.toList()) : getLobbies();
    }

    public static void newLobby(String name, User user, List<Category> categories) throws LobbyExistsError {
        if (isLobby(name)) {
            throw new LobbyExistsError("This lobby name is already taken!");
        }
        Lobby lobby = new Lobby(name, new ArrayList<>(){{add(user);}}, categories);
        lobbyLookup.put(name, lobby);
        userLookup.put(user, lobby);
    }

    public static void joinLobby(User user, Lobby lobby) {
        if (userLookup.containsKey(user)) {
            userLookup.get(user).getPlayers().remove(user);
        }
        lobby.getPlayers().add(user);
        userLookup.put(user, lobby);
    }

    public static void leaveLobby(User user) {
        if (userLookup.containsKey(user)) {
            Lobby lobby = userLookup.get(user);
            lobby.getPlayers().remove(user);
            if (lobby.getPlayers().isEmpty()) {
                lobbyLookup.remove(lobby.getName());
            }
        }
        userLookup.remove(user);
    }
}
