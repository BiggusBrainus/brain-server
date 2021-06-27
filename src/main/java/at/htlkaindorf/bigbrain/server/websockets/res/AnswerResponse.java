package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import at.htlkaindorf.bigbrain.server.websockets.res.errors.AnswerError;
import at.htlkaindorf.bigbrain.server.websockets.res.errors.ConnectToLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version BigBrain v1
 * @since 09.06.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
public class AnswerResponse extends WebSocketResponse {
    private AnswerError error;

    public AnswerResponse(AnswerError error) {
        this.action = LobbyGameHandlerActions.ANSWER;
        this.error = error;
    }
}
