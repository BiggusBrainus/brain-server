package at.htlkaindorf.bigbrain.server.db.access;

import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.db.DB_Access;
import at.htlkaindorf.bigbrain.server.db.DB_Properties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DB Access class for all operations using all
 * tables in the questions database.
 * @author m4ttm00ny
 */

public class Questions extends DB_Access {
    private static Questions theInstance = null;

    public static Questions getInstance() throws SQLException, ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new Questions();
        }
        return theInstance;
    }

    private Questions() throws SQLException, ClassNotFoundException {
        loadProperties();
        connect();
    }

    private void loadProperties() {
        this.db_url = DB_Properties.getPropertyValue("users_url");
        this.db_driver = DB_Properties.getPropertyValue("driver");
        this.db_user = DB_Properties.getPropertyValue("db_username");
        this.db_pass = DB_Properties.getPropertyValue("db_password");
    }

    public List<Category> getAllCategories() throws SQLException {
        Statement stat = db.getStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM categories");
        List<Category> categories = new ArrayList<>();
        while (rs.next()) {
            categories.add(new Category(rs.getInt("cid"), rs.getString("title")));
        }
        return categories;
    }
}
