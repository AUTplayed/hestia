package at.karriere.controller;


import at.karriere.services.CliService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class CliController {
    final static Logger LOGGER = Logger.getLogger(CliController.class);

    CliService service;

    @Autowired
    public CliController(CliService service) {
        this.service = service;
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public String test(){
        LOGGER.info("GET test");
        return "working";
    }

    @RequestMapping(value = "/cli",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public String cli(@RequestParam(required = false,name = "hostname")String hostname,
                      @RequestParam(required = false,name = "port")Integer port,
                      @RequestParam(required = true,name = "command")String command){
        LOGGER.info("GET cli");
        String[] splitCommand = command.split(" ");
        String[] args = Arrays.copyOfRange(splitCommand,1,splitCommand.length);
        String result = service.executeCommand(hostname,port,splitCommand[0], args);
        LOGGER.info(result);
        return result;
    }

}
