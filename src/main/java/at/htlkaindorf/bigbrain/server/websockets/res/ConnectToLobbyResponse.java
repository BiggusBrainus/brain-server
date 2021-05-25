package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.websockets.res.errors.ConnectToLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectToLobbyResponse extends WebSocketResponse {
    private ConnectToLobbyError error;
}
