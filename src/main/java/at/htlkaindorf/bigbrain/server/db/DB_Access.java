package at.htlkaindorf.bigbrain.server.db;

import at.htlkaindorf.bigbrain.server.db.access.Questions;
import at.htlkaindorf.bigbrain.server.db.access.Users;
import at.htlkaindorf.bigbrain.server.errors.UnknownCategoryException;

import java.sql.SQLException;

/**
 * Template class for all DB access classes.
 * @author m4ttm00ny
 */

public class DB_Access {
    protected static DB_Access theInstance = null;
    protected DB_Database db;
    protected String db_url;
    protected String db_driver;
    protected String db_user;
    protected String db_pass;

    public void connect() throws SQLException, ClassNotFoundException {
        if (db != null) {
            db.disconnect();
        }
        db = new DB_Database(db_url, db_driver, db_user, db_pass);
        db.connect();
    }

    public void disconnect() throws SQLException {
        if (db == null) {
            return;
        }
        db.disconnect();
        db = null;
    }

    public boolean isConnected() {
        return db != null && db.isConnected();
    }
}
