package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.RegisterError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
