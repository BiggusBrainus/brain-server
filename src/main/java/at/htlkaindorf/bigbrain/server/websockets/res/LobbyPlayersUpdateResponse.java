package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LobbyPlayersUpdateResponse extends WebSocketResponse {
    private List<User> players;

    public LobbyPlayersUpdateResponse(List<User> players) {
        this.action = LobbyGameHandlerActions.LOBBY_PLAYERS_UPDATE;
        this.players = players;
    }
}
