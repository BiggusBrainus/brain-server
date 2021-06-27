package at.htlkaindorf.bigbrain.server.rest.res;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version BigBrain v1
 * @since 11.06.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
public class ConfirmResponse extends RESTResponse {
    public ConfirmResponse(boolean success) {
        super(success);
    }
}
