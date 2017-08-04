package at.karriere.components;

import at.karriere.entities.Connection;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class DefaultHostComponent {

    final static Logger LOGGER = Logger.getLogger(DefaultHostComponent.class);

    public void check(Connection connection){
        //check if no host and port are passed
        if(connection.getHostname() == null && connection.getPort() == null) {

            //Load default properties
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(System.getProperty("user.dir") + "/config/application.properties"));
            } catch (IOException e) {
                LOGGER.error("Could not load application properties",e);
            }

            //Set default properties
            connection.setHostname(prop.getProperty("redis.host"));
            connection.setPort(Integer.valueOf(prop.getProperty("redis.port")));
        }
    }
}
