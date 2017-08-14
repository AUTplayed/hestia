package at.karriere.hestia.service;

import at.karriere.hestia.AbstractIT;
import org.assertj.core.api.Condition;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class KeysServiceIT extends AbstractIT {

    @Autowired
    CliService cliService;
    @Autowired
    KeysService keysService;

    @Test
    public void testKeys() {
        cliService.executeCommand(host, port, "SET TEST VALUE");
        cliService.executeCommand(host, port, "SET TEST0 VALUE0");
        String keys = keysService.keys(0L, 10L, null, host, port, 0);
        try {
            JSONObject json = new JSONObject(keys);
            assertThat(json.has("cursor")).as("check if json has cursor property").isTrue();
            assertThat(json.has("keys")).as("check if json has keys property").isTrue();
            assertThat(((JSONArray)json.get("keys")).length()).as("check if keys jsonarray has length of > 1").is(new Condition<Integer>() {
                @Override
                public boolean matches(Integer value) {
                    return value > 1;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
