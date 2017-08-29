package at.karriere.hestia.component;

import at.karriere.hestia.entity.CommandContainer;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DBCommandWrapperComponent {

    public CommandContainer wrap(CommandContainer commandContainer, int db) {
        CommandContainer wrapContainer = new CommandContainer();
        wrapContainer.setCommand("EVAL");
        String basearg = "redis.call('SELECT'," + db + ") return redis.call('" + commandContainer.getCommand() + "'";

        String[] args = commandContainer.getArgs();
        String wraparg = "";
        if (args.length > 0) {
            wraparg += ",";
        }
        for (int i = 0; i < args.length; i++) {
            boolean numberic = StringUtils.isNumeric(args[i]);
            if (!numberic) {
                wraparg += "'";
            }
            wraparg += args[i];
            if (!numberic) {
                wraparg += "'";
            }
            if (i != args.length - 1) {
                wraparg += ",";
            }
        }
        wraparg += ")";
        basearg += wraparg;

        String[] finalargs = {basearg, "0"};
        wrapContainer.setArgs(finalargs);
        return wrapContainer;
    }
}
