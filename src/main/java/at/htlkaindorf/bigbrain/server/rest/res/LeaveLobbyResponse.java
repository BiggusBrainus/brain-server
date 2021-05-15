package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.LeaveLobbyError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LeaveLobbyResponse {
    private boolean success;
    private LeaveLobbyError error;

    public LeaveLobbyResponse() {
        this.success = true;
    }

    public LeaveLobbyResponse(LeaveLobbyError error) {
        this.success = false;
        this.error = error;
    }
}
