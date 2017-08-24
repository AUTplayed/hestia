package at.karriere.hestia.service;

import at.karriere.hestia.AbstractIT;
import org.assertj.core.api.Condition;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class NamespaceScheduleServiceIT extends AbstractIT {

    @Autowired
    NamespaceScheduleService namespaceScheduleService;
    @Autowired
    CliService cliService;

    @Test
    public void test() {
        cliService.executeCommand(host, port, "SET j:key1 val");
        cliService.executeCommand(host, port, "SET j:key2 val");
        cliService.executeCommand(host, port, "SET j:key3 val");
        cliService.executeCommand(host, port, "SET c:key val");
        cliService.executeCommand(host, port, "SET c:key2 val");
        namespaceScheduleService.scan(host, port);
        String res = cliService.executeCommand(host, port, "GET info");
        JSONObject jsonObject = new JSONObject(res);
        assertThat(jsonObject.keySet().contains("j")).as("check if j is in the info keyset").isTrue();
        assertThat(jsonObject.getJSONObject("j").getLong("count")).as("check amount of j's").is(new Condition<Long>() {
            @Override
            public boolean matches(Long value) {
                return value >= 3;
            }
        });
        assertThat(jsonObject.keySet().contains("c")).as("check if c is in the info keyset").isTrue();
        assertThat(jsonObject.getJSONObject("c").getLong("count")).as("check amount of c's").is(new Condition<Long>() {
            @Override
            public boolean matches(Long value) {
                return value >= 2;
            }
        });
    }
}
