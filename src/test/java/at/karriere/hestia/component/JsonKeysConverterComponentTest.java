package at.karriere.hestia.component;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonKeysConverterComponentTest {

    JsonKeysConverterComponent jsonKeysConverterComponent = new JsonKeysConverterComponent();

    @Test
    public void testEmpty() {
        String string = "";
        String json = jsonKeysConverterComponent.convert(string);
        assertThat(json).as("check if empty json object").isEqualTo("{}");
    }

    @Test
    public void testOnlyCursor() {
        String string = "cursor";
        String json = jsonKeysConverterComponent.convert(string);
        assertThat(json).as("check if only cursor").isEqualTo("{\"cursor\":\"cursor\"}");
    }

    @Test
    public void testCursorAndKeys() {
        String string = "cursor\nkey1\nkey2";
        String json = jsonKeysConverterComponent.convert(string);
        assertThat(json).as("check if valid object").isEqualTo("{\"cursor\":\"cursor\",\"keys\":[\"key1\",\"key2\"]}");
    }
}
