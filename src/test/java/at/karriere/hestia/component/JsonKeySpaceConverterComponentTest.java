package at.karriere.hestia.component;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonKeySpaceConverterComponentTest {

    JsonKeySpaceConverterComponent jsonKeySpaceConverterComponent = new JsonKeySpaceConverterComponent();

    @Test
    public void testEmpty() {
        String test = "";
        String json = jsonKeySpaceConverterComponent.convert(test);
        assertThat(json).as("check if empty array is returned").isEqualTo("[]");

    }

    @Test
    public void test() {
        String test = "this gets cleared\na\nb";
        String json = jsonKeySpaceConverterComponent.convert(test);
        assertThat(json).as("check if json array is returned").isEqualTo("[\"a\",\"b\"]");
    }
}
