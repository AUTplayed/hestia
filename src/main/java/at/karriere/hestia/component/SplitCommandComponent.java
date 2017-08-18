package at.karriere.hestia.component;

import at.karriere.hestia.entity.CommandContainer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SplitCommandComponent {


    public CommandContainer split(String commandString) {
        //Split args from command
        List<String> list = new LinkedList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|(?<!\\\\)\".+?(?<!\\\\)\")\\s*").matcher(commandString);
        while(m.find()) {
            String match = m.group(1);
            if(match.startsWith("\"") && match.endsWith("\""))
            {
                match = match.substring(1, match.length() - 1);
                match = match.replaceAll("\\\\\"", "\"");
            }
            list.add(match);
        }
        String[] splitCommand = new String[list.size()];
        splitCommand = list.toArray(splitCommand);
        String[] args = Arrays.copyOfRange(splitCommand,1, splitCommand.length);

        return new CommandContainer(splitCommand[0], args);
    }
}
