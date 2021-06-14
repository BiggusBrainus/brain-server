package at.htlkaindorf.bigbrain.server.beans;

import at.htlkaindorf.bigbrain.server.game.Game;
import at.htlkaindorf.bigbrain.server.game.LobbyManager;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandler;
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
 * @version BigBrain v1
 * @since 07.04.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lobby {
    private String name;
    private List<User> players;
    private List<Category> categories;
    private boolean hidden;
    @JsonIgnore
    private Game game;
    @JsonIgnore
    private Map<User, WebSocketSession> connections = new HashMap<>();

    public Lobby(String name, List<User> players, List<Category> categories, boolean hidden) {
        this.name = name;
        this.players = players;
        this.categories = categories;
        this.hidden = hidden;
    }

    /**
     * Indicates, whether or not the lobby is already
     * playing a game.
     * @return Whether or not the lobby is currently in-game.
     */
    public boolean isInGame() {
        return game != null;
    }

    /**
     * Broadcasts a WebSocketResponse via all the
     * joined user's WebSocket sessions.
     * @param res   The WebSocketResponse that should be broadcast.
     */
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
        disconnect.forEach(u -> LobbyManager.leaveLobby(u));
    }
}
