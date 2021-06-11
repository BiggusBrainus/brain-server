package at.htlkaindorf.bigbrain.server.rest.res;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfirmResponse extends RESTResponse {
    public ConfirmResponse(boolean success) {
        super(success);
    }
}
