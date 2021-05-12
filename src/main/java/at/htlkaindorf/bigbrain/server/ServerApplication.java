package at.htlkaindorf.bigbrain.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 *
 * @author m4ttm00ny
 */

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class })
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
