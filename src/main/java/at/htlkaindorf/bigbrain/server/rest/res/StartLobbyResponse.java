package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.JoinLobbyError;
import at.htlkaindorf.bigbrain.server.rest.res.errors.StartLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StartLobbyResponse {
    private boolean success;
    private StartLobbyError error;

    public StartLobbyResponse() {
        this.success = true;
    }

    public StartLobbyResponse(StartLobbyError error) {
        this.success = false;
        this.error = error;
    }
}
