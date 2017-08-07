package at.karriere.hestia.component;

import at.karriere.hestia.entity.Connection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class DefaultHostComponent {

    final static Logger LOGGER = Logger.getLogger(DefaultHostComponent.class);

    @Value("${redis.host}")
    private String defaultHostname;
    @Value("${redis.port}")
    private Integer defaultPort;


    public void check(Connection connection) {
        //check if no host or port are passed
        //and set default properties
        if(connection.getHostname() == null) {
            connection.setHostname(defaultHostname);
        }
        if(connection.getPort() == null) {
            connection.setPort(defaultPort);
        }
    }
}
