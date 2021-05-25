package at.htlkaindorf.bigbrain.server.websockets.req;

import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketRequestWrapper {
    private LobbyGameHandlerActions action;
    private WebSocketRequest body;
}
