package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.beans.Rank;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandlerActions;
import lombok.Data;

import java.util.AbstractMap;
import java.util.List;

@Data
public class EndOfGameResponse extends WebSocketResponse {
    private List<Rank> ranking;

    public EndOfGameResponse(List<Rank> ranking) {
        this.action = LobbyGameHandlerActions.END_OF_GAME;
        this.ranking = ranking;
    }
}
