package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.LeaveLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version BigBrain v1
 * @since 15.05.2021
 * @author m4ttm00ny
 */

@Data
public class LeaveLobbyResponse extends RESTResponse {
    private LeaveLobbyError error;

    public LeaveLobbyResponse() {
        this.success = true;
    }

    public LeaveLobbyResponse(LeaveLobbyError error) {
        this.success = false;
        this.error = error;
    }
}
