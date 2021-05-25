package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketResponseWrapper {
    private LobbyGameHandlerActions action;
    private WebSocketResponse body;
}
