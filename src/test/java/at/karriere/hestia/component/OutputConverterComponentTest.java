package at.karriere.hestia.component;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class OutputConverterComponentTest {

    OutputConverterComponent outputConverterComponent = new OutputConverterComponent();

    @Test
    public void testString() {
        String string = "test";
        byte[] bytes = string.getBytes();
        String result = outputConverterComponent.stringify(bytes);
        Assertions.assertThat(result).as("check string(byte[]) conversion").isEqualTo(string);
    }

    @Test
    public void testLong() {
        Long testlong = 10L;
        String result = outputConverterComponent.stringify(testlong);
        Assertions.assertThat(result).as("check Long conversion").isEqualTo(10);
    }

    @Test
    public void testList() {
        List list = new LinkedList();
        list.add("a".getBytes());
        list.add("b".getBytes());
        list.add("c".getBytes());
        String result = outputConverterComponent.stringify(list);
        String expected = "a\nb\nc";
        Assertions.assertThat(result).as("check list conversion").isEqualTo(expected);
    }
}
