package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.JoinLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version BigBrain v1
 * @since 15.05.2021
 * @author m4ttm00ny
 */

@Data
public class JoinLobbyResponse extends RESTResponse {
    private JoinLobbyError error;

    public JoinLobbyResponse() {
        this.success = true;
    }

    public JoinLobbyResponse(JoinLobbyError error) {
        this.success = false;
        this.error = error;
    }
}
