package at.htlkaindorf.bigbrain.server.game;

import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.beans.Lobby;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.errors.LobbyExistsError;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Game {
    private static List<Lobby> lobbies = new ArrayList<>();

    public static boolean isLobby(String name) {
        return lobbies.stream().anyMatch(l -> l.getName().equals(name));
    }

    public static List<Lobby> getLobbies() {
        return lobbies;
    }

    public static List<Lobby> getLobbiesByCategories(List<Category> categories) {
        return categories != null && categories.size() > 0 ? lobbies.stream().filter(l -> l.getCategories().stream().anyMatch(categories::contains)).collect(Collectors.toList()) : getLobbies();
    }

    public static void newLobby(String name, User user, List<Category> categories) throws LobbyExistsError {
        if (isLobby(name)) {
            throw new LobbyExistsError("This lobby name is already taken!");
        }
        lobbies.add(new Lobby(name, new ArrayList<>(){{add(user);}}, categories));
    }
}
