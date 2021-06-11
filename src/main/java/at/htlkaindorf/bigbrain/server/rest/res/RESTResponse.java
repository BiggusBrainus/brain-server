package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.RESTError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RESTResponse {
    protected boolean success;
    protected RESTError error;

    public RESTResponse(RESTError error) {
        this.success = false;
        this.error = error;
    }
}
