package at.karriere.hestia;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    @PostConstruct
    public void init() throws IOException {
        /*
        File file = new File(System.getProperty("user.dir")+"/log4j2.json");
        if (file.exists()) {
            LOGGER.error("config exists");
            ConfigurationSource source = new ConfigurationSource(new FileInputStream(file));
            Configurator.initialize(null, source);
        }
        */
        //Sentry.init("https://public:private@host:port/1");
        LOGGER.info("Started");

    }
}
