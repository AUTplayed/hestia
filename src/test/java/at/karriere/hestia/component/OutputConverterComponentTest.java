package at.karriere.hestia.component;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OutputConverterComponentTest {

    OutputConverterComponent outputConverterComponent = new OutputConverterComponent();

    @Test
    public void testString() {
        String string = "test";
        byte[] bytes = string.getBytes();
        String result = outputConverterComponent.stringify(bytes);
        assertThat(result).as("check string(byte[]) conversion").isEqualTo(string);
    }

    @Test
    public void testLong() {
        Long testlong = 10L;
        String result = outputConverterComponent.stringify(testlong);
        assertThat(result).as("check Long conversion").isEqualTo("10");
    }

    @Test
    public void testList() {
        List list = new LinkedList();
        list.add("a".getBytes());
        list.add("b".getBytes());
        list.add("c".getBytes());
        String result = outputConverterComponent.stringify(list);
        String expected = "a\nb\nc";
        assertThat(result).as("check list conversion").isEqualTo(expected);
    }

    @Test
    public void testNull() {
        Object o = null;
        String result = outputConverterComponent.stringify(o);
        assertThat(result).as("check null conversion").isEqualTo("");
    }

    @Test
    public void testNullList() {
        List list = new LinkedList();
        list.add("a".getBytes());
        list.add("b".getBytes());
        list.add(null);
        list.add(null);
        list.add("c".getBytes());
        String result = outputConverterComponent.stringify(list);
        assertThat(result).as("check null list conversion").isEqualTo("a\nb\nc");
    }

    @Test
    public void testMaxLong() {
        Long max = Long.MAX_VALUE;
        String result = outputConverterComponent.stringify(max);
        assertThat(result).as("check long maxval conversion").isEqualTo(String.valueOf(Long.MAX_VALUE));
    }

    @Test
    public void testEmptyList() {
        List list = new LinkedList();
        String result = outputConverterComponent.stringify(list);
        assertThat(result).as("check empty list conversion").isEqualTo("");
    }

    @Test
    public void testListWithEmptyEmbeddedList() {
        List list = new LinkedList();
        list.add("header".getBytes());
        list.add(new LinkedList<>());
        String result = outputConverterComponent.stringify(list);
        assertThat(result).as("check list with embedded empty list").isEqualTo("header");
    }

    @Test
    public void testListWithEmbeddedList() {
        List list = new LinkedList();
        List embed = new LinkedList();
        list.add("header".getBytes());
        embed.add("test".getBytes());
        list.add(embed);
        String result = outputConverterComponent.stringify(list);
        assertThat(result).as("check list with embedded list").isEqualTo("header\ntest");
    }
}
