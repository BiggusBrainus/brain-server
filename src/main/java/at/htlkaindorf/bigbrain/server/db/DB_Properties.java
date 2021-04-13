package at.htlkaindorf.bigbrain.server.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @author m4ttm00ny
 */

public class DB_Properties {
    private static final Properties PROPS = new Properties();
    private static final Path PROP_FILE = Paths.get(System.getProperty("user.dir"), "src", "res", "db_access.properties");

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
