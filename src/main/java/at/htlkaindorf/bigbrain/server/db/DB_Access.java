package at.htlkaindorf.bigbrain.server.db;

import java.sql.SQLException;

/**
 * Template class for all DB access classes.
 * @version BigBrain v1
 * @since 13.04.2021
 * @author m4ttm00ny
 */

public class DB_Access {
    protected static DB_Access theInstance = null;
    protected DB_Database db;
    protected String db_url;
    protected String db_driver;
    protected String db_user;
    protected String db_pass;

    /**
     * Connect to the database.
     * @throws SQLException             Establishing a connection failed.
     * @throws ClassNotFoundException   The Postgres driver class couldn't be loaded.
     */
    public void connect() throws SQLException, ClassNotFoundException {
        if (db != null) {
            db.disconnect();
        }
        db = new DB_Database(db_url, db_driver, db_user, db_pass);
        db.connect();
    }

    /**
     * Disconnect from the database.
     * @throws SQLException     Disconnecting from the database failed.
     */
    public void disconnect() throws SQLException {
        if (db == null) {
            return;
        }
        db.disconnect();
        db = null;
    }

    /**
     * Checks, whether or not a connection to the
     * database has been established.
     * @return Whether or not a connection hsa been established.
     */
    public boolean isConnected() {
        return db != null && db.isConnected();
    }
}
