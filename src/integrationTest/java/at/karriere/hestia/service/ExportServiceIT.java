package at.karriere.hestia.service;

import at.karriere.hestia.AbstractIT;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ExportServiceIT extends AbstractIT {

    @Autowired
    ExportService exportService;
    @Autowired
    CliService cliService;

    @Test
    public void testJson() {
        cliService.executeCommand(host, port, "SET foo1 bar1");
        cliService.executeCommand(host, port, "SET foo2 bar2");
        String response = exportService.export(host, port, 0, "foo1\nfoo2", "json");
        JSONArray jsonArray = new JSONArray(response);
        JSONObject one = jsonArray.getJSONObject(0);
        assertThat(one.get("key")).as("check key of first jsonobject").isEqualTo("foo1");
        assertThat(one.get("value")).as("check key of first jsonobject").isEqualTo("bar1");
        JSONObject two = jsonArray.getJSONObject(1);
        assertThat(two.get("key")).as("check key of first jsonobject").isEqualTo("foo2");
        assertThat(two.get("value")).as("check key of first jsonobject").isEqualTo("bar2");
    }

    @Test
    public void testCsv() {
        cliService.executeCommand(host, port, "SET foo1 bar1");
        cliService.executeCommand(host, port, "SET foo2 bar2");
        String response = exportService.export(host, port, 0, "foo1\nfoo2", "csv");
        String expected = "sep=;\nkey;value\nfoo1;bar1\nfoo2;bar2";
        assertThat(response).as("check if csv is valid").isEqualTo(expected);
    }
}
