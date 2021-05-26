package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class StartLobbyResponse extends WebSocketResponse {
    public StartLobbyResponse() {
        this.action = LobbyGameHandlerActions.START_GAME;
    }
}
