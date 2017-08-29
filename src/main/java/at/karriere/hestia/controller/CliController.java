package at.karriere.hestia.controller;


import at.karriere.hestia.component.SplitCommandComponent;
import at.karriere.hestia.service.CliService;
import at.karriere.hestia.service.DBWrapperCliService;
import at.karriere.hestia.service.KeyspaceService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class CliController {
    final static Logger LOGGER = Logger.getLogger(CliController.class);

    SplitCommandComponent splitCommandComponent;
    DBWrapperCliService dbWrapperCliService;

    @Autowired
    public CliController(SplitCommandComponent splitCommandComponent, DBWrapperCliService dbWrapperCliService) {
        this.splitCommandComponent = splitCommandComponent;
        this.dbWrapperCliService = dbWrapperCliService;
    }

    //Test route for testing
    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String test() {
        LOGGER.info("GET test");
        return "working";
    }

    /**
     * Executes a specified command with specified args (separated with spaces) on specified host and port redis server, returning results in String form
     *
     * @param host
     * @param port
     * @param command
     * @return
     */
    @RequestMapping(value = "/cli", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String cli(@RequestParam(required = false, name = "host") String host,
                      @RequestParam(required = false, name = "port") Integer port,
                      @RequestParam(required = true, name = "command") String command,
                      @RequestParam(required = false, name = "db") Integer db) {
        LOGGER.info("GET /cli");

        //Execute command
        return dbWrapperCliService.wrapAndExecute(host, port, command, db);
    }

}