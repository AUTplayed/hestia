package at.karriere.hestia.service;

import at.karriere.hestia.AbstractIT;
import org.assertj.core.api.Condition;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyspaceServiceIT extends AbstractIT {

    @Autowired
    KeyspaceService keyspaceService;
    @Autowired
    DBWrapperCliService dbWrapperCliService;

    @Test
    public void testTwoKeyspaces() {
        dbWrapperCliService.wrapAndExecute(host, port, "SET TEST0 VALUE0", 0);
        dbWrapperCliService.wrapAndExecute(host, port, "SET TEST00 VALUE00", 0);
        dbWrapperCliService.wrapAndExecute(host, port, "SET TEST1 VALUE1", 1);
        String result = keyspaceService.getKeySpaces(host, port);
        try {
            JSONArray json = new JSONArray(result);
            assertThat(json.length()).as("check if json array contains more than 1 keyspace").is(new Condition<Integer>() {
                @Override
                public boolean matches(Integer value) {
                    return value > 1;
                }
            })
            assertThat(json.get(0).toString().split("=")[1]).as("check key size in db0 greater than 1").is(new Condition<String>() {
                @Override
                public boolean matches(String value) {
                    return Long.valueOf(value) > 1;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
