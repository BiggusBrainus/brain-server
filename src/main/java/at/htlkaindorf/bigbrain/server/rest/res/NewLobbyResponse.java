package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.NewLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @version BigBrain v1
 * @since 12.05.2021
 * @author m4ttm00ny
 */

@Data
public class NewLobbyResponse extends RESTResponse {
    private NewLobbyError error;

    public NewLobbyResponse() {
        this.success = true;
    }

    public NewLobbyResponse(NewLobbyError error) {
        this.success = false;
        this.error = error;
    }
}
