package at.karriere.hestia.service;

import at.karriere.hestia.AbstractIT;
import org.assertj.core.api.Condition;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class NamespaceServiceIT extends AbstractIT{
    @Autowired
    NamespaceScheduleService namespaceScheduleService;
    @Autowired
    NamespaceService namespaceService;
    @Autowired
    CliService cliService;

    @Test
    public void test() {
        cliService.executeCommand(host, port, "SET j:job1 val");
        cliService.executeCommand(host, port, "SET c:canonical1 val");
        namespaceScheduleService.scan(host,port);
        String info = namespaceService.getNamespaces(host, port, 0);
        JSONObject json = new JSONObject(info);
        assertThat(json.keySet().size()).as("check json keyset size").is(new Condition<Integer>() {
            @Override
            public boolean matches(Integer value) {
                return value > 1;
            }
        });

        JSONObject j = json.getJSONObject("j");
        assertThat(j.getLong("count")).as("check count of j").isGreaterThan(0);
        JSONObject c = json.getJSONObject("c");
        assertThat(c.getLong("count")).as("check count of c").isGreaterThan(0);
    }
}
