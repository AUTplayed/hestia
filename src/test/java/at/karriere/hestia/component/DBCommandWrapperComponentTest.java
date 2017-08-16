package at.karriere.hestia.component;

import at.karriere.hestia.entity.CommandContainer;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DBCommandWrapperComponentTest {

    DBCommandWrapperComponent dbCommandWrapperComponent = new DBCommandWrapperComponent();
    SplitCommandComponent splitCommandComponent = new SplitCommandComponent();
    private int db = 1;
    private String baseArg = "redis.call('SELECT',"+db+") return redis.call(";


    @Test
    public void testNoArgs() {
        String command = "DBSIZE";
        CommandContainer container = splitCommandComponent.split(command);
        CommandContainer wrapped = dbCommandWrapperComponent.wrap(container,1);
        assertThat(wrapped.getCommand()).as("check if wrapped command is EVAL").isEqualTo("EVAL");
        String[] wrappedArgs = wrapped.getArgs();
        assertThat(wrappedArgs.length).as("check if args are 2").isEqualTo(2);
        assertThat(wrappedArgs[1]).as("check if arg 2 is \"0\"").isEqualTo("0");
        assertThat(wrappedArgs[0]).as("check arg concat").isEqualTo(
                baseArg+"'DBSIZE')"
        );
    }

    @Test
    public void testOneArg() {
        String command = "GET foo";
        CommandContainer container = splitCommandComponent.split(command);
        CommandContainer wrapped = dbCommandWrapperComponent.wrap(container,1);
        assertThat(wrapped.getCommand()).as("check if wrapped command is EVAL").isEqualTo("EVAL");
        String[] wrappedArgs = wrapped.getArgs();
        assertThat(wrappedArgs.length).as("check if args are 2").isEqualTo(2);
        assertThat(wrappedArgs[1]).as("check if arg 2 is \"0\"").isEqualTo("0");
        assertThat(wrappedArgs[0]).as("check arg concat").isEqualTo(
                baseArg+"'GET','foo')"
        );
    }

    @Test
    public void testTwoArgs() {
        String command = "SET foo bar";
        CommandContainer container = splitCommandComponent.split(command);
        CommandContainer wrapped = dbCommandWrapperComponent.wrap(container,1);
        assertThat(wrapped.getCommand()).as("check if wrapped command is EVAL").isEqualTo("EVAL");
        String[] wrappedArgs = wrapped.getArgs();
        assertThat(wrappedArgs.length).as("check if args are 2").isEqualTo(2);
        assertThat(wrappedArgs[1]).as("check if arg 2 is \"0\"").isEqualTo("0");
        assertThat(wrappedArgs[0]).as("check arg concat").isEqualTo(
                baseArg+"'SET','foo','bar')"
        );
    }

    @Test
    public void testIntegerArg() {
        String command = "SET foo 5";
        CommandContainer container = splitCommandComponent.split(command);
        CommandContainer wrapped = dbCommandWrapperComponent.wrap(container,1);
        assertThat(wrapped.getCommand()).as("check if wrapped command is EVAL").isEqualTo("EVAL");
        String[] wrappedArgs = wrapped.getArgs();
        assertThat(wrappedArgs.length).as("check if args are 2").isEqualTo(2);
        assertThat(wrappedArgs[1]).as("check if arg 2 is \"0\"").isEqualTo("0");
        assertThat(wrappedArgs[0]).as("check arg concat").isEqualTo(
                baseArg+"'SET','foo',5)"
        );
    }
}
