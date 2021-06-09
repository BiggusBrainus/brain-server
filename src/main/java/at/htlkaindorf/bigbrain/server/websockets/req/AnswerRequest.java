package at.htlkaindorf.bigbrain.server.websockets.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest extends WebSocketRequest {
    private String token;
    private int question;
    private String answer;
}
