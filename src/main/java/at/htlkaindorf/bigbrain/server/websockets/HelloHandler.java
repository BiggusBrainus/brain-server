package at.htlkaindorf.bigbrain.server.websockets;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * A test websocket handler - listens
 * on the /ws/hello endpoint.
 * @version BigBrain v1
 * @since 12.05.2021
 * @author m4ttm00ny
 */

public class HelloHandler extends TextWebSocketHandler {
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            super.handleTextMessage(session, message);
            String msg = String.valueOf(message.getPayload());
            System.out.println(msg);
            session.sendMessage(new TextMessage("hello!"));
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
        }
    }
}
