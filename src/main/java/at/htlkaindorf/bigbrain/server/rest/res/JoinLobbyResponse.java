package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.JoinLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class JoinLobbyResponse {
    private boolean success;
    private JoinLobbyError error;

    public JoinLobbyResponse() {
        this.success = true;
    }

    public JoinLobbyResponse(JoinLobbyError error) {
        this.success = false;
        this.error = error;
    }
}
