package at.htlkaindorf.bigbrain.server.websockets.res;

import at.htlkaindorf.bigbrain.server.beans.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LobbyPlayersUpdateResponse extends WebSocketResponse {
    private List<User> players;
}
