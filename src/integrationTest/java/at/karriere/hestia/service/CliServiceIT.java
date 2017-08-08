package at.karriere.hestia.service;

import at.karriere.hestia.TestApplication;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CliServiceIT extends TestApplication {


    @Autowired
    CliService cliService;


    @Before
    public void startDocker() {
        startDockerContainer();
    }

    @Test
    public void testSetAndGet() {
        String setResult = cliService.executeCommand("localhost",getRedisPort(),"SET TEST VALUE");
        Assertions.assertThat(setResult).as("check if set was successful").isEqualTo("OK");

        String getResult = cliService.executeCommand("localhost",getRedisPort(),"GET TEST");
        Assertions.assertThat(getResult).as("check if get returns correct value").isEqualTo("VALUE");
    }

    @After
    public void stopDocker() {
        stopDockerContainer();
    }
}
