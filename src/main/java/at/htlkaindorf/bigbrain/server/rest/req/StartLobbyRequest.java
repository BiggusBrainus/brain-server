package at.htlkaindorf.bigbrain.server.rest.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartLobbyRequest {
    private String token;
    private String lobby;
}
