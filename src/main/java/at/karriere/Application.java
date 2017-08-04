package at.karriere;



import at.karriere.repositories.Repository;
import io.sentry.Sentry;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class Application {

    Repository repository;

    @Autowired
    public Application(Repository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
    private static final Logger LOGGER = LogManager.getLogger(Application.class);
    @PostConstruct
    public void init() throws IOException {
        /*
        File file = new File(System.getProperty("user.dir")+"/log4j2.json");
        if(file.exists()){
            LOGGER.error("config exists");
            ConfigurationSource source = new ConfigurationSource(new FileInputStream(file));
            Configurator.initialize(null, source);
        }
        */
        //Sentry.init("https://public:private@host:port/1");
        LOGGER.info("Started");

    }
}
