package at.htlkaindorf.bigbrain.server.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Wrapper class for dealing with
 * the properties file storing the database
 * information.
 * @version BigBrain v1
 * @since 13.04.2021
 * @author m4ttm00ny
 */

public class DB_Properties {
    private static final Properties PROPS = new Properties();
    private static final Path PROP_FILE = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "at", "htlkaindorf", "bigbrain", "server", "res", "db_access.properties");

    static {
        try {
            PROPS.load(new FileInputStream(PROP_FILE.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPropertyValue(String key) {
        return PROPS.getProperty(key);
    }
}
