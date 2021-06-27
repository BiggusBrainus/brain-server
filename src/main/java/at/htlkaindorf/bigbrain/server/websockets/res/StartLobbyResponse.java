package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version BigBrain v1
 * @since 26.05.2021
 * @author m4ttm00ny
 */

@Data
public class StartLobbyResponse extends WebSocketResponse {
    public StartLobbyResponse() {
        this.action = LobbyGameHandlerActions.START_GAME;
    }
}
