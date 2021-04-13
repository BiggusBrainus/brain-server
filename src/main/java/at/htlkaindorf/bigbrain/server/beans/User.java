package at.htlkaindorf.bigbrain.server.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a registered user of the app.
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int uid;
    private String username;
    private String email;
    private String password;
    private Lobby lobby;

    public User(int uid, String username, String email, String password) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
