package at.htlkaindorf.bigbrain.server.rest.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version BigBrain v1
 * @since 15.05.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveLobbyRequest {
    String token;
}
