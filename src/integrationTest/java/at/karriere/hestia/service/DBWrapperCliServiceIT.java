package at.karriere.hestia.service;

import at.karriere.hestia.AbstractIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class DBWrapperCliServiceIT extends AbstractIT {

    @Autowired
    DBWrapperCliService dbWrapperCliService;

    @Test
    public void testSetAndGet() {
        String result = dbWrapperCliService.wrapAndExecute(host, port, "SET TEST1 VALUE", 1);
        assertThat(result).as("check if set was successful on db1").isEqualTo("OK");
        result = dbWrapperCliService.wrapAndExecute(host, port, "GET TEST1", 1);
        assertThat(result).as("check if get returns right value on db1").isEqualTo("VALUE");
    }

}
