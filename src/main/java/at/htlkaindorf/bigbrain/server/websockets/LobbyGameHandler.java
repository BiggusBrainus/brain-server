package at.htlkaindorf.bigbrain.server.websockets;

import at.htlkaindorf.bigbrain.server.auth.Authenticator;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.errors.InvalidSignatureError;
import at.htlkaindorf.bigbrain.server.errors.NotJoinedError;
import at.htlkaindorf.bigbrain.server.errors.UnknownUserException;
import at.htlkaindorf.bigbrain.server.game.GameManager;
import at.htlkaindorf.bigbrain.server.rest.res.LeaveLobbyResponse;
import at.htlkaindorf.bigbrain.server.rest.res.errors.LeaveLobbyError;
import at.htlkaindorf.bigbrain.server.websockets.req.ConnectToLobbyRequest;
import at.htlkaindorf.bigbrain.server.websockets.req.WebSocketRequestWrapper;
import at.htlkaindorf.bigbrain.server.websockets.res.ConnectToLobbyResponse;
import at.htlkaindorf.bigbrain.server.websockets.res.WebSocketResponse;
import at.htlkaindorf.bigbrain.server.websockets.res.WebSocketResponseWrapper;
import at.htlkaindorf.bigbrain.server.websockets.res.errors.ConnectToLobbyError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author m4ttm00ny
 */

public class LobbyGameHandler extends TextWebSocketHandler {

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            super.handleTextMessage(session, message);
            WebSocketRequestWrapper req = mapper.readValue(String.valueOf(message.getPayload()), WebSocketRequestWrapper.class);
            switch (req.getAction()) {
                case CONNECT_TO_LOBBY:
                    connectToLobby(session, req);
                    break;
            }
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendMessage(WebSocketSession session, WebSocketResponse res, LobbyGameHandlerActions action) throws IOException {
        session.sendMessage(new TextMessage(mapper.writeValueAsString(new WebSocketResponseWrapper(action, res))));
    }

    private void connectToLobby(WebSocketSession session, WebSocketRequestWrapper wrappedReq) throws IOException {
        ConnectToLobbyRequest req = (ConnectToLobbyRequest) wrappedReq.getBody();
        try {
            User user = Authenticator.getUser(req.getToken());
            GameManager.connectToLobby(user, session);
        } catch (SQLException|ClassNotFoundException e) {
            sendMessage(session, new ConnectToLobbyResponse(ConnectToLobbyError.OTHER_ERROR), LobbyGameHandlerActions.CONNECT_TO_LOBBY);
        } catch (UnknownUserException|InvalidSignatureError e) {
            sendMessage(session, new ConnectToLobbyResponse(ConnectToLobbyError.AUTH_FAILURE), LobbyGameHandlerActions.CONNECT_TO_LOBBY);
        } catch (NotJoinedError notJoinedError) {
            sendMessage(session, new ConnectToLobbyResponse(ConnectToLobbyError.NOT_A_MEMBER), LobbyGameHandlerActions.CONNECT_TO_LOBBY);
        }
    }
}
