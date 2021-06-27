package at.htlkaindorf.bigbrain.server.websockets.req;

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
@AllArgsConstructor
public class AnswerRequest extends WebSocketRequest {
    private String token;
    private int question;
    private String answer;
}
