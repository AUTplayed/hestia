package at.karriere.hestia.component;

import at.karriere.hestia.entity.State;
import at.karriere.hestia.service.CliService;
import at.karriere.hestia.service.ExactKeysService;
import at.karriere.hestia.service.KeysService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class ExactKeysComponentTest {

    KeysService keysService;
    ExactKeysComponent exactKeysComponent;

    @Before
    public void prepare() {
        keysService = Mockito.mock(KeysService.class);
    }

    @Test
    public void testQueue() {
        when(keysService.keys(anyLong(), anyLong(), anyString(), anyString(), anyInt(), anyInt())).thenReturn("123\nkey1\nkey2").thenReturn("234\nkey3\nkey4\nkey5");
        exactKeysComponent = new ExactKeysComponent(keysService);
        State state = new State();
        state.setKeys("");
        String keys1 = exactKeysComponent.getKeys(state, 3L , "key*" ,null, null, null);
        assertThat(keys1).as("check if first call returned right values").isEqualTo("key1\nkey2\nkey3");
        assertThat(state.getCursor()).as("check if cursor was set correctly").isEqualTo(234L);
        assertThat(state.getFromQueue(2L)).as("check if queue was filled correctly").isEqualTo("key4\nkey5");
    }

    @Test
    public void testExact() {
        when(keysService.keys(anyLong(), anyLong(), anyString(), anyString(), anyInt(), anyInt())).thenReturn("123\nkey1\nkey2").thenReturn("234\nkey3\nkey4\nkey5");
        exactKeysComponent = new ExactKeysComponent(keysService);
        State state = new State();
        state.setKeys("");
        String keys1 = exactKeysComponent.getKeys(state, 2L , "key*" ,null, null, null);
        assertThat(keys1).as("check if first call returned right values").isEqualTo("key1\nkey2");
        assertThat(state.getCursor()).as("check if cursor was set correctly").isEqualTo(123L);
        assertThat(state.getFromQueue(2L)).as("check if queue was not filled").isEqualTo("");
    }

    @Test
    public void testEndOfDB() {
        when(keysService.keys(anyLong(), anyLong(), anyString(), anyString(), anyInt(), anyInt())).thenReturn("123\nkey1\nkey2").thenReturn("0\nkey3");
        exactKeysComponent = new ExactKeysComponent(keysService);
        State state = new State();
        state.setKeys("");
        String keys1 = exactKeysComponent.getKeys(state, 5L , "key*" ,null, null, null);
        assertThat(keys1).as("check if first call returned right values").isEqualTo("key1\nkey2\nkey3");
        assertThat(state.getCursor()).as("check if cursor was set correctly").isEqualTo(0L);
        assertThat(state.getFromQueue(2L)).as("check if queue was not filled").isEqualTo("");
    }

    @Test
    public void testMeanIncreaseWhenNoResult() {
        when(keysService.keys(anyLong(), anyLong(), anyString(), anyString(), anyInt(), anyInt())).thenReturn("123").thenReturn("234").thenReturn("345").thenReturn("456\nkey1");
        exactKeysComponent = new ExactKeysComponent(keysService);
        State state = new State();
        state.setKeys("");
        String keys1 = exactKeysComponent.getKeys(state, 1L , "key*" ,null, null, null);
        assertThat(keys1).as("check if first call returned right values").isEqualTo("key1");
        assertThat(state.getCursor()).as("check if cursor was set correctly").isEqualTo(456L);
        assertThat(state.getFromQueue(2L)).as("check if queue was not filled").isEqualTo("");
        assertThat(state.getMean()).as("check if mean increased greatly").isGreaterThan(100);
    }
}
