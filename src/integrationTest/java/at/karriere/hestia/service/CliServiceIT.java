package at.karriere.hestia.service;

import at.karriere.hestia.AbstractIT;
import at.karriere.hestia.DockerTestListener;
import at.karriere.hestia.DockerTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class CliServiceIT extends AbstractIT {

    @Autowired
    CliService cliService;

    @Test
    public void testSetAndGet() {
        String setResult = cliService.executeCommand(host, port,"SET TEST VALUE");
        assertThat(setResult).as("check if set was successful").isEqualTo("OK");

        String getResult = cliService.executeCommand(host, port,"GET TEST");
        assertThat(getResult).as("check if get returns correct value").isEqualTo("VALUE");
    }

}
