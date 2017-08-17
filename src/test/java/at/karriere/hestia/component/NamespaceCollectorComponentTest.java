package at.karriere.hestia.component;

import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class NamespaceCollectorComponentTest {

    NamespaceCollectorComponent namespaceCollectorComponent = new NamespaceCollectorComponent();

    @Test
    public void test() {
        HashMap<String, Long> map = new HashMap<>();
        String[] keys = {"asd", "a:key1", "a:key2", "b:key1"};
        namespaceCollectorComponent.collect(keys, map);
        assertThat(map.size()).as("check key map size").isEqualTo(2);
        assertThat(map.get("a")).as("check a namespace count").isEqualTo(2L);
        assertThat(map.get("b")).as("check b namespace count").isEqualTo(1L);
    }
}
