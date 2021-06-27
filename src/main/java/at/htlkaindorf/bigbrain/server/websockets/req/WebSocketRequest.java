package at.htlkaindorf.bigbrain.server.websockets.req;

import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version BigBrain v1
 * @since 26.05.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketRequest {
    private LobbyGameHandlerActions action;
}
