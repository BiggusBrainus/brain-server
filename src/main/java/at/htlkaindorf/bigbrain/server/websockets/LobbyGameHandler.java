package at.htlkaindorf.bigbrain.server.websockets;

import at.htlkaindorf.bigbrain.server.auth.Authenticator;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.errors.InvalidSignatureError;
import at.htlkaindorf.bigbrain.server.errors.NotJoinedError;
import at.htlkaindorf.bigbrain.server.errors.UnknownUserException;
import at.htlkaindorf.bigbrain.server.game.GameManager;
import at.htlkaindorf.bigbrain.server.websockets.req.AnswerRequest;
import at.htlkaindorf.bigbrain.server.websockets.req.ConnectToLobbyRequest;
import at.htlkaindorf.bigbrain.server.websockets.req.WebSocketRequest;
import at.htlkaindorf.bigbrain.server.websockets.res.AnswerResponse;
import at.htlkaindorf.bigbrain.server.websockets.res.ConnectToLobbyResponse;
import at.htlkaindorf.bigbrain.server.websockets.res.WebSocketResponse;
import at.htlkaindorf.bigbrain.server.websockets.res.errors.AnswerError;
import at.htlkaindorf.bigbrain.server.websockets.res.errors.ConnectToLobbyError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The WebSocket handler for all lobby & game
 * actions - listens on the /ws/game endpoint.
 * @version BigBrain v1
 * @since 25.05.2021
 * @author m4ttm00ny
 */

public class LobbyGameHandler extends TextWebSocketHandler {

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            super.handleTextMessage(session, message);
            String msg = String.valueOf(message.getPayload());
            WebSocketRequest req = mapper.readValue(msg, WebSocketRequest.class);
            switch (req.getAction()) {
                case CONNECT_TO_LOBBY:
                    connectToLobby(session, msg);
                    break;
                case ANSWER:
                    answerQuestion(session, msg);
                    break;
            }
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendMessage(WebSocketSession session, WebSocketResponse res) throws IOException {
        session.sendMessage(new TextMessage(mapper.writeValueAsString(res)));
    }

    private void connectToLobby(WebSocketSession session, String msg) throws IOException {
        ConnectToLobbyRequest req = mapper.readValue(msg, ConnectToLobbyRequest.class);
        // System.out.printf("[*] MSG: \"%s\"\n", msg);
        // System.out.printf("[*] NEW CONNECTION TO LOBBY USING TOKEN %s!\n", req.getToken());
        try {
            User user = Authenticator.getUser(req.getToken());
            GameManager.connectToLobby(user, session);
        } catch (SQLException|ClassNotFoundException e) {
            sendMessage(session, new ConnectToLobbyResponse(ConnectToLobbyError.OTHER_ERROR));
        } catch (UnknownUserException|InvalidSignatureError e) {
            sendMessage(session, new ConnectToLobbyResponse(ConnectToLobbyError.AUTH_FAILURE));
        } catch (NotJoinedError notJoinedError) {
            sendMessage(session, new ConnectToLobbyResponse(ConnectToLobbyError.NOT_A_MEMBER));
        }
    }

    private void answerQuestion(WebSocketSession session, String msg) throws IOException {
        AnswerRequest req = mapper.readValue(msg, AnswerRequest.class);
        try {
            User user = Authenticator.getUser(req.getToken());
            user.getLobby().getGame().submitAnswer(user, req.getQuestion(), req.getAnswer());
        } catch (SQLException|ClassNotFoundException e) {
            sendMessage(session, new AnswerResponse(AnswerError.OTHER_ERROR));
        } catch (UnknownUserException|InvalidSignatureError e) {
            sendMessage(session, new AnswerResponse(AnswerError.AUTH_FAILURE));
        }
    }
}
