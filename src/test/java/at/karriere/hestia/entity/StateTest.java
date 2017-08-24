package at.karriere.hestia.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StateTest {

    @Test
    public void testConnection() {
        State state = new State();
        assertThat(state.isSameConnection(null, null, null)).as("check if all null = true").isTrue();
        state.setConnection("localhost", 1234, 1);
        assertThat(state.isSameConnection("localhost", 1234, 1)).as("check if all same = true").isTrue();
        state.setConnection("local", 124, 3);
        assertThat(state.isSameConnection("localhost", 1234, 1)).as("check if some different = false").isFalse();
        state.setConnection("localhost", 1234, 0);
        assertThat(state.isSameConnection("localhost", 1234, null)).as("check if edge cases is true").isTrue();
    }

    @Test
    public void testQueue() {
        String[] arr = {"a","b","c","d"};
        State state = new State();
        state.addToQueue(arr);
        String res = state.getFromQueue(3L);
        assertThat(res).as("check if queue returned right values").isEqualTo("a\nb\nc");
        assertThat(state.getSizeOfQueue()).as("check queue size").isEqualTo(1);
    }
}
