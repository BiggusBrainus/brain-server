package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.NewLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewLobbyResponse {
    private boolean success;
    private NewLobbyError error;

    public NewLobbyResponse() {
        this.success = true;
    }

    public NewLobbyResponse(NewLobbyError error) {
        this.error = error;
    }
}
