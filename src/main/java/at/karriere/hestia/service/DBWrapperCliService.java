package at.karriere.hestia.service;

import at.karriere.hestia.component.DBCommandWrapperComponent;
import at.karriere.hestia.component.SplitCommandComponent;
import at.karriere.hestia.entity.CommandContainer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBWrapperCliService {
    final static Logger LOGGER = Logger.getLogger(DBWrapperCliService.class);

    private CliService cliService;
    private SplitCommandComponent splitCommandComponent;
    private DBCommandWrapperComponent dbCommandWrapperComponent;

    @Autowired
    public DBWrapperCliService(CliService cliService, SplitCommandComponent splitCommandComponent, DBCommandWrapperComponent dbCommandWrapperComponent) {
        this.cliService = cliService;
        this.splitCommandComponent = splitCommandComponent;
        this.dbCommandWrapperComponent = dbCommandWrapperComponent;
    }

    public String wrapAndExecute(String host, Integer port, String command, Integer db) {
        LOGGER.info(command);
        if (db == null || db == 0) {
            return cliService.executeCommand(host, port, command);
        }
        CommandContainer commandContainer = splitCommandComponent.split(command);
        CommandContainer wrapContainer = dbCommandWrapperComponent.wrap(commandContainer, db);

        LOGGER.info(wrapContainer.getCommand() + " \"" + wrapContainer.getArgs()[0] + "\" " + wrapContainer.getArgs()[1]);
        return cliService.executeCommand(host, port, wrapContainer);
    }
}
