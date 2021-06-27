package at.htlkaindorf.bigbrain.server.websockets.req;

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
@AllArgsConstructor
public class ConnectToLobbyRequest extends WebSocketRequest {
    private String token;
}
