package at.karriere.hestia.service;

import at.karriere.hestia.AbstractIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class InfoServiceIT extends AbstractIT {

    @Autowired
    InfoService infoService;

    @Test
    public void test() {
        String info = infoService.getInfo(host, port);
        assertThat(info.startsWith("# Server")).as("check if info starts with header # Server").isTrue();
        assertThat(info.contains("redis_version:")).as("check if info contains redis version").isTrue();
    }
}
