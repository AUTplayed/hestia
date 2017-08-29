package at.karriere.hestia.service;

import at.karriere.hestia.AbstractIT;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExactKeysServiceIT extends AbstractIT {

    @Autowired
    ExactKeysService exactKeysService;
    @Autowired
    CliService cliService;

    @Test
    public void test() {
        cliService.executeCommand(host, port, "set foo1 val");
        cliService.executeCommand(host, port, "set asd1 val");
        cliService.executeCommand(host, port, "set asd2 val");
        cliService.executeCommand(host, port, "set foo2 val");
        cliService.executeCommand(host, port, "set foo3 val");
        JSONObject json = new JSONObject(exactKeysService.keysJson(0L, 3L, "foo*", host, port, 0, null));
        assertThat(json.getString("cookie")).as("check cookie size").hasSize(32);
        assertThat(json.has("cursor")).as("check if json has cursor").isTrue();
        JSONArray jsonKeys = json.getJSONArray("keys");
        List keys = jsonKeys.toList();
        assertThat(keys).as("check keys size is as requested").hasSize(3);
        assertThat(keys.contains("foo1")).as("check if foo1 is contained").isTrue();
        assertThat(keys.contains("foo2")).as("check if foo2 is contained").isTrue();
        assertThat(keys.contains("foo3")).as("check if foo3 is contained").isTrue();

    }
}