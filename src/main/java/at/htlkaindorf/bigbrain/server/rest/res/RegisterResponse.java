package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.RegisterError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version BigBrain v1
 * @since 20.04.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse extends RESTResponse {
    private String token;
    private RegisterError error;

    public RegisterResponse(String token) {
        this.success = true;
        this.token = token;
    }

    public RegisterResponse(RegisterError error) {
        this.success = false;
        this.error = error;
    }
}
