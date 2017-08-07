package at.karriere.hestia.component;

import at.karriere.hestia.entity.CommandContainer;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SplitCommandComponent {


    public CommandContainer split(String commandString) {
        //Split args from command
        String[] splitCommand = commandString.split(" ");
        String[] args = Arrays.copyOfRange(splitCommand,1, splitCommand.length);

        return new CommandContainer(splitCommand[0], args);
    }
}
