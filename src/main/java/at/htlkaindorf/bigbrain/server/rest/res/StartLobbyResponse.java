package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.JoinLobbyError;
import at.htlkaindorf.bigbrain.server.rest.res.errors.StartLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @version BigBrain v1
 * @since 25.05.2021
 * @author m4ttm00ny
 */

@Data
public class StartLobbyResponse extends RESTResponse {
    private StartLobbyError error;

    public StartLobbyResponse() {
        this.success = true;
    }

    public StartLobbyResponse(StartLobbyError error) {
        this.success = false;
        this.error = error;
    }
}
