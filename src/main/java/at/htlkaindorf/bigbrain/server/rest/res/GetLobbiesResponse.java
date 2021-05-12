package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.beans.Lobby;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetLobbiesResponse {
    private boolean success;
    private List<Lobby> lobbies;
}
