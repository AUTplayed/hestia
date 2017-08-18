package at.karriere.hestia.component;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KeySpaceCollectorComponentTest {

    KeySpaceCollectorComponent keySpaceCollectorComponent = new KeySpaceCollectorComponent();

    @Test
    public void test() {
        String input = "# Keyspace\ndb0:keys=123\ndb11:keys=123\n";
        Integer[] keyspaces = keySpaceCollectorComponent.collect(input);
        assertThat(keyspaces.length).as("check keyspace count").isEqualTo(2);
        assertThat(keyspaces[0]).as("check if first keyspace is the correct number").isEqualTo(0);
        assertThat(keyspaces[1]).as("check if second keyspace is the correct number").isEqualTo(11);
    }

    @Test
    public void testNoKeyspaces() {
        String input = "# Keyspace\n";
        Integer[] keyspaces = keySpaceCollectorComponent.collect(input);
        assertThat(keyspaces.length).as("check keyspace count").isEqualTo(0);
    }
}
