package at.karriere.hestia.component;

import org.assertj.core.api.Condition;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExportFormatComponentTest {

    ExportFormatComponent exportFormatComponent = new ExportFormatComponent();
    String[] keyArray = {"key1", "key2", "key3"};
    String[] valueArray = {"val1", "val2", "val3"};

    @Test
    public void testJson() {
        String json = exportFormatComponent.format(keyArray, valueArray, "json");

        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        assertThat(jsonObject.get("key")).as("check if first json key is correct").isEqualTo("key1");
        assertThat(jsonObject.get("value")).as("check if first json value is correct").isEqualTo("val1");
        jsonObject = jsonArray.getJSONObject(1);
        assertThat(jsonObject.get("key")).as("check if second json key is correct").isEqualTo("key2");
        assertThat(jsonObject.get("value")).as("check if second json value is correct").isEqualTo("val2");
        jsonObject = jsonArray.getJSONObject(2);
        assertThat(jsonObject.get("key")).as("check if third json key is correct").isEqualTo("key3");
        assertThat(jsonObject.get("value")).as("check if third json value is correct").isEqualTo("val3");
    }

    @Test
    public void testCsv() {
        String csv = exportFormatComponent.format(keyArray, valueArray, "csv");
        String expected = "sep=;\nkey;value\nkey1;val1\nkey2;val2\nkey3;val3";
        assertThat(csv).as("check if csv is valid").isEqualTo(expected);
    }

    @Test
    public void testInvalid() {
        String invalid = exportFormatComponent.format(keyArray, valueArray, "xhtlm");
        assertThat(invalid).as("check if invalid format returns empty string").isEqualTo("");
    }
}
