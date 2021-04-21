package at.htlkaindorf.bigbrain.server.db.access;

import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.db.DB_Access;
import at.htlkaindorf.bigbrain.server.db.DB_Properties;
import at.htlkaindorf.bigbrain.server.errors.UnknownUserException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DB Access class for all operations using the
 * `users` table in the user database.
 * @author m4ttm00ny
 */

public class UsersAccess extends DB_Access {
    private static UsersAccess theInstance = null;

    private final String GET_USER_BY_UID_QRY    = "SELECT uid, username, email, password FROM users WHERE uid = ?";
    private final String GET_USER_BY_NAME_QRY   = "SELECT uid, username, email, password FROM users WHERE username = ?";
    private final String INSERT_USER_QRY        = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

    private PreparedStatement getUserByUidStat  = null;
    private PreparedStatement getUserByNameStat = null;
    private PreparedStatement insertUserStat    = null;

    public static UsersAccess getInstance() throws SQLException, ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new UsersAccess();
        }
        return theInstance;
    }

    private UsersAccess() throws SQLException, ClassNotFoundException {
        loadProperties();
        connect();
    }

    private void loadProperties() {
        this.db_url = DB_Properties.getPropertyValue("users_url");
        this.db_driver = DB_Properties.getPropertyValue("driver");
        this.db_user = DB_Properties.getPropertyValue("users_username");
        this.db_pass = DB_Properties.getPropertyValue("users_password");
    }

    public List<User> getAllUsers() throws SQLException {
        Statement stat = db.getStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM users");
        List<User> users = new ArrayList<>();
        while (rs.next()) users.add(User.fromResultSet(rs));
        return users;
    }

    public User getUserByUid(int uid) throws SQLException, UnknownUserException {
        if (getUserByUidStat == null) {
            getUserByUidStat = db.getConnection().prepareStatement(GET_USER_BY_UID_QRY);
        }
        getUserByUidStat.setInt(1, uid);
        ResultSet rs = getUserByUidStat.executeQuery();
        if (!rs.next()) throw new UnknownUserException(String.format("No user with uid %d found!", uid));
        return User.fromResultSet(rs);
    }

    public User getUserByName(String username) throws SQLException, UnknownUserException {
        if (getUserByNameStat == null) {
            getUserByNameStat = db.getConnection().prepareStatement(GET_USER_BY_NAME_QRY);
        }
        getUserByNameStat.setString(1, username);
        ResultSet rs = getUserByNameStat.executeQuery();
        if (!rs.next()) throw new UnknownUserException(String.format("No user with username \"%s\" found!", username));
        return User.fromResultSet(rs);
    }

    public void createUser(User user) throws SQLException {
        if (insertUserStat == null) {
            insertUserStat = db.getConnection().prepareStatement(INSERT_USER_QRY);
        }
        insertUserStat.setString(1, user.getUsername());
        insertUserStat.setString(2, user.getEmail());
        insertUserStat.setString(3, user.getPassword());
        insertUserStat.executeUpdate();
    }
}
