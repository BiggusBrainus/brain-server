package at.htlkaindorf.bigbrain.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Represents the actual database. Can
 * be connected/disconnected to, etc.
 * @version BigBrain v1
 * @since 13.04.2021
 * @author m4ttm00ny
 */

public class DB_Database {
    private final String DB_URL;
    private final String DB_DRIVER;
    private final String DB_USER;
    private final String DB_PASS;
    private Connection conn;
    private DB_Cached_Connection cachedConn;

    public DB_Database(String db_url, String db_driver, String db_user, String db_pass) throws ClassNotFoundException {
        this.DB_URL = db_url;
        this.DB_DRIVER = db_driver;
        this.DB_USER = db_user;
        this.DB_PASS = db_pass;
        Class.forName(db_driver);
    }

    public void connect() throws SQLException {
        if (conn != null) {
            conn.close();
        }
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        cachedConn = new DB_Cached_Connection(conn);
    }

    public void disconnect() throws SQLException {
        if (conn != null) {
            cachedConn.close();
            conn.close();
            conn = null;
        }
    }

    public boolean isConnected() {
        return conn != null;
    }

    public Connection getConnection() {
        return conn;
    }

    public Statement getStatement() throws SQLException {
        if (conn == null || cachedConn == null) {
            throw new RuntimeException("Database connection error!");
        }
        return cachedConn.getStatement();
    }

    public void releaseStatement(Statement statement) {
        if (conn == null || cachedConn == null) {
            throw new RuntimeException("Database connection error!");
        }
        cachedConn.releaseStatement(statement);
    }
}
