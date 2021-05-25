package at.htlkaindorf.bigbrain.server.websockets.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectToLobbyRequest extends WebSocketRequest {
    private String token;
}
