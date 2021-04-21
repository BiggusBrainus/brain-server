package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.RegisterError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private boolean success;
    private String token;
    private RegisterError error;

    public RegisterResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public RegisterResponse(boolean success, RegisterError error) {
        this.success = success;
        this.error = error;
    }
}
