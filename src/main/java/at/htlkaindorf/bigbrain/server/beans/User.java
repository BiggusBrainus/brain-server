package at.htlkaindorf.bigbrain.server.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Represents a registered user of the app.
 * @version BigBrain v1
 * @since 07.04.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int uid;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    @ToStringExclude
    private Lobby lobby;

    public User(int uid, String username, String email, String plainPassword) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = hashPassword(plainPassword);
    }

    public User(int uid, String username, String email, byte[] hashedPassword) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = new String(hashedPassword);
    }

    public User(String username, String email, String plainPassword) {
        this.username = username;
        this.email = email;
        this.password = hashPassword(plainPassword);
    }

    /**
     * Checks whether or not the given password is actually
     * the user's password.
     * @param password  The password to compare the user's password to.
     * @return Whether or not the given password is the correct password.
     */
    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    /**
     * Hashes and returns the given password using the bcrypt algorithm.
     * @link https://en.wikipedia.org/wiki/Bcrypt
     * @param password  The password string to be hashed.
     * @return The bcrypt hash of the given password string.
     */
    public static String hashPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * Construct a new User object from the ResultSet returned
     * from a Postgres query.
     * @param rs    The ResultSet object.
     * @return A User object containing the ResultSet's info.
     * @throws SQLException     The ResultSet seems to be missing columns.
     */
    public static User fromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getInt("uid"), rs.getString("username"), rs.getString("email"), rs.getString("password").getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uid == user.uid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
