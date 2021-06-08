package at.htlkaindorf.bigbrain.server.beans;

import at.htlkaindorf.bigbrain.server.game.Game;
import at.htlkaindorf.bigbrain.server.game.GameManager;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandler;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import at.htlkaindorf.bigbrain.server.websockets.res.LobbyPlayersUpdateResponse;
import at.htlkaindorf.bigbrain.server.websockets.res.WebSocketResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a game lobby. Multiple users can be
 * in a lobby as players. The lobby can have multiple
 * card categories selected. Cards will be picked from these.
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lobby {
    private String name;
    private List<User> players;
    private List<Category> categories;
    @JsonIgnore
    private Game game;
    @JsonIgnore
    private Map<User, WebSocketSession> connections = new HashMap<>();

    public Lobby(String name, List<User> players, List<Category> categories) {
        this.name = name;
        this.players = players;
        this.categories = categories;
    }

    public boolean isInGame() {
        return game != null;
    }

    public void broadcast(WebSocketResponse res) {
        List<User> disconnect = new ArrayList<>();
        getConnections().forEach((u, s) -> {
            try {
                LobbyGameHandler.sendMessage(s, res);
            } catch (IOException e) {
            } catch (IllegalStateException e) {
                disconnect.add(u);
            }
        });
        disconnect.forEach(u -> GameManager.leaveLobby(u));
    }
}
