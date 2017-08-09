package at.karriere.hestia.controller;


import at.karriere.hestia.component.SplitCommandComponent;
import at.karriere.hestia.entity.CommandContainer;
import at.karriere.hestia.service.CliService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.Map;

@ManagedBean
@RestController
public class CliController {
    final static Logger LOGGER = Logger.getLogger(CliController.class);

    CliService service;
    SplitCommandComponent splitCommandComponent;

    @Autowired
    public CliController(CliService service, SplitCommandComponent splitCommandComponent) {
        this.service = service;
        this.splitCommandComponent = splitCommandComponent;
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
     * @param hostname
     * @param port
     * @param command
     * @return
     */
    @RequestMapping(value = "/cli", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String cli(@RequestParam(required = false, name = "hostname") String hostname,
                      @RequestParam(required = false, name = "port") Integer port,
                      @RequestParam(required = true, name = "command") String command) {
        LOGGER.info("GET cli");

        //Execute command
        String result = service.executeCommand(hostname, port,command);

        return result;
    }

}
