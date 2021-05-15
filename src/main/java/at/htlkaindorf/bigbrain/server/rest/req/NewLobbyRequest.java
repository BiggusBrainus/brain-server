package at.htlkaindorf.bigbrain.server.rest.req;

import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.beans.Lobby;
import lombok.Data;

import java.util.List;

@Data
public class NewLobbyRequest {
    private String token;
    private Lobby lobby;
    private List<Integer> categories;
}
