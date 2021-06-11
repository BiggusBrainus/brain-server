package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.beans.Lobby;
import at.htlkaindorf.bigbrain.server.rest.res.errors.GetLobbiesError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetLobbiesResponse extends RESTResponse {
    private List<Lobby> lobbies;
    private GetLobbiesError error;

    public GetLobbiesResponse(List<Lobby> lobbies) {
        this.success = true;
        this.lobbies = lobbies;
    }

    public GetLobbiesResponse(GetLobbiesError error) {
        this.success = false;
        this.error = error;
    }
}
