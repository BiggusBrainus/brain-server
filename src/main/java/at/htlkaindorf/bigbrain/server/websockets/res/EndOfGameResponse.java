package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import lombok.Data;

import java.util.AbstractMap;
import java.util.List;

@Data
public class EndOfGameResponse extends WebSocketResponse {
    private List<AbstractMap.SimpleEntry<User, Long>> ranking;

    public EndOfGameResponse(List<AbstractMap.SimpleEntry<User, Long>> ranking) {
        this.action = LobbyGameHandlerActions.END_OF_GAME;
        this.ranking = ranking;
    }
}
