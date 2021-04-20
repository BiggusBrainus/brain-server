package at.htlkaindorf.bigbrain.server.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public User(int uid, String username, String email, String plainPassword) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(plainPassword);
    }

    public User(int uid, String username, String email, byte[] hashedPassword) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = new String(hashedPassword);
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    public static User fromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getInt("uid"), rs.getString("username"), rs.getString("email"), rs.getString("password").getBytes(StandardCharsets.UTF_8));
    }
}
