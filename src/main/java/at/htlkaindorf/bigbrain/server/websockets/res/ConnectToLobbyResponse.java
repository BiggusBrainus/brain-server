package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import at.htlkaindorf.bigbrain.server.websockets.res.errors.ConnectToLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version BigBrain v1
 * @since 25.05.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
public class ConnectToLobbyResponse extends WebSocketResponse {
    private ConnectToLobbyError error;

    public ConnectToLobbyResponse(ConnectToLobbyError error) {
        this.action = LobbyGameHandlerActions.CONNECT_TO_LOBBY;
        this.error = error;
    }
}
