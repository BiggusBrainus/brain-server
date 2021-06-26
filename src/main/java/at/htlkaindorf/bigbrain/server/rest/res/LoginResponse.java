package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.rest.res.errors.LoginError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse extends RESTResponse {
    private int uid;
    private String token;
    private LoginError error;

    public LoginResponse(int uid, String token) {
        this.uid = uid;
        this.success = true;
        this.token = token;
    }

    public LoginResponse(LoginError error) {
        this.success = false;
        this.error = error;
    }
}
