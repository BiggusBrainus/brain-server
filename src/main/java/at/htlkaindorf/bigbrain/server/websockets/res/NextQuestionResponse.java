package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.beans.Question;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import lombok.Data;

/**
 * @version BigBrain v1
 * @since 26.05.2021
 * @author m4ttm00ny
 */

@Data
public class NextQuestionResponse extends WebSocketResponse {
    private Question question;

    public NextQuestionResponse(Question question) {
        this.action = LobbyGameHandlerActions.NEXT_QUESTION;
        this.question = question;
    }
}
