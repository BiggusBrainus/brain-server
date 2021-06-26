package at.htlkaindorf.bigbrain.server.db.access;

import at.htlkaindorf.bigbrain.server.beans.Rank;
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
 * @version BigBrain v1
 * @since 21.04.2021
 * @author m4ttm00ny
 */

public class UsersAccess extends DB_Access {
    private static UsersAccess theInstance = null;

    private final String GET_USER_BY_UID_QRY    = "SELECT uid, username, email, password FROM users WHERE uid = ?";
    private final String GET_USER_BY_EMAIL_QRY  = "SELECT Uid, username, email, password FROM users WHERE email = ?";
    private final String GET_USER_BY_NAME_QRY   = "SELECT uid, username, email, password FROM users WHERE username = ?";
    private final String INSERT_USER_QRY        = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
    private final String GET_RANKING_QRY        = "SELECT uid, username, email, password, COUNT(winner) \"wins\" FROM users u LEFT OUTER JOIN games g ON u.uid = g.winner GROUP BY uid, username, email, password ORDER BY wins DESC LIMIT ?";
    private final String INSERT_GAME_QRY        = "INSERT INTO games (winner) VALUES (?)";

    private PreparedStatement getUserByUidStat      = null;
    private PreparedStatement getUserByEmailStat    = null;
    private PreparedStatement getUserByNameStat     = null;
    private PreparedStatement insertUserStat        = null;
    private PreparedStatement getRankingStat        = null;
    private PreparedStatement insertGameStat        = null;

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

    /**
     * Load the connection details and credentials using
     * the DB_Properties wrapper.
     */
    private void loadProperties() {
        this.db_url = DB_Properties.getPropertyValue("users_url");
        this.db_driver = DB_Properties.getPropertyValue("driver");
        this.db_user = DB_Properties.getPropertyValue("users_username");
        this.db_pass = DB_Properties.getPropertyValue("users_password");
    }

    /**
     * Get a list of all users from the users database.
     * @return The list of User objects.
     * @throws SQLException     The SQL query failed.
     */
    public List<User> getAllUsers() throws SQLException {
        Statement stat = db.getStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM users");
        List<User> users = new ArrayList<>();
        while (rs.next()) users.add(User.fromResultSet(rs));
        return users;
    }

    /**
     * Get the user with the given user ID from the
     * users database.
     * @param uid   The user's user ID.
     * @return The User object identified by the given ID.
     * @throws SQLException         The SQL query failed.
     * @throws UnknownUserException No user has the given user ID.
     */
    public User getUserByUid(int uid) throws SQLException, UnknownUserException {
        if (getUserByUidStat == null) {
            getUserByUidStat = db.getConnection().prepareStatement(GET_USER_BY_UID_QRY);
        }
        getUserByUidStat.setInt(1, uid);
        ResultSet rs = getUserByUidStat.executeQuery();
        if (!rs.next()) throw new UnknownUserException(String.format("No user with uid %d found!", uid));
        return User.fromResultSet(rs);
    }

    public User getUserByEmail(String email) throws SQLException, UnknownUserException {
        if (getUserByEmailStat == null) {
            getUserByEmailStat = db.getConnection().prepareStatement(GET_USER_BY_EMAIL_QRY);
        }
        getUserByEmailStat.setString(1, email);
        ResultSet rs = getUserByEmailStat.executeQuery();
        if (!rs.next()) throw new UnknownUserException(String.format("No user with email \"%s\" found!", email));
        return User.fromResultSet(rs);
    }

    /**
     * Get the user with the given username from
     * the users database.
     * @param username  The user's username.
     * @return The User object identified by the given ID.
     * @throws SQLException         The SQL query failed.
     * @throws UnknownUserException No user has the given username.
     */
    public User getUserByName(String username) throws SQLException, UnknownUserException {
        if (getUserByNameStat == null) {
            getUserByNameStat = db.getConnection().prepareStatement(GET_USER_BY_NAME_QRY);
        }
        getUserByNameStat.setString(1, username);
        ResultSet rs = getUserByNameStat.executeQuery();
        if (!rs.next()) throw new UnknownUserException(String.format("No user with username \"%s\" found!", username));
        return User.fromResultSet(rs);
    }

    /**
     * Store a new user in the Postgres database.
     * @param user  The User object to be stored.
     * @throws SQLException     The SQL query failed.
     */
    public void createUser(User user) throws SQLException {
        if (insertUserStat == null) {
            insertUserStat = db.getConnection().prepareStatement(INSERT_USER_QRY);
        }
        insertUserStat.setString(1, user.getUsername());
        insertUserStat.setString(2, user.getEmail());
        insertUserStat.setString(3, user.getPassword());
        insertUserStat.executeUpdate();
    }

    /**
     * Store the result of a game in the Postgres
     * database.
     * @param winner    The user that won the game.
     * @throws SQLException     The SQL query failed.
     */
    public void addGame(User winner) throws SQLException {
        if (insertGameStat == null) {
            insertGameStat = db.getConnection().prepareStatement(INSERT_GAME_QRY);
        }
        insertGameStat.setInt(1, winner.getUid());
        insertGameStat.executeUpdate();
    }

    /**
     * Get the top players (in terms of victories)
     * from the Postgres database.
     * @param n     The number of top players to get.
     * @return A list of the top n players.
     * @throws SQLException     The SQL query failed.
     */
    public List<Rank> getTopN(int n) throws SQLException {
        if (getRankingStat == null) {
            getRankingStat = db.getConnection().prepareStatement(GET_RANKING_QRY);
        }
        getRankingStat.setInt(1, n);
        ResultSet rs = getRankingStat.executeQuery();
        List<Rank> ranking = new ArrayList<>();
        while (rs.next()) ranking.add(new Rank(User.fromResultSet(rs), (long) rs.getInt("wins")));
        return ranking;
    }
}
