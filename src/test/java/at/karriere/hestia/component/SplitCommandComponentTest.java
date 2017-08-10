package at.karriere.hestia.component;

import at.karriere.hestia.entity.CommandContainer;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SplitCommandComponentTest {

    SplitCommandComponent splitCommandComponent = new SplitCommandComponent();

    @Test
    public void test() {
        String commandString = "SET FOO BAR";

        CommandContainer commandContainer = splitCommandComponent.split(commandString);
        assertThat(commandContainer.getCommand().equals("SET"));
        assertThat(commandContainer.getArgs().length).as("Check length").isEqualTo(2);
        assertThat(commandContainer.getArgs()[0]).as("Check arg0").isEqualTo("FOO");
        assertThat(commandContainer.getArgs()[1]).as("Check arg1").isEqualTo("BAR");
    }

    @Test
    public void testQuotes() {
        String commandString = "EVAL \"return redis.call('DBSIZE')\" 0";
        CommandContainer commandContainer = splitCommandComponent.split(commandString);
        assertThat(commandContainer.getCommand().equals("EVAL"));
        assertThat(commandContainer.getArgs().length).as("Check length").isEqualTo(2);
        assertThat(commandContainer.getArgs()[0]).as("Check arg0").isEqualTo("\"return redis.call('DBSIZE')\"");
        assertThat(commandContainer.getArgs()[1]).as("Check arg1").isEqualTo("0");
    }
}
