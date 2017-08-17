package at.karriere.hestia.controller;

import at.karriere.hestia.service.*;
import org.apache.log4j.Logger;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    final static Logger LOGGER = Logger.getLogger(ApiController.class);

    KeysService keysService;
    DBWrapperCliService dbWrapperCliService;
    CliService cliService;
    KeyspaceService keyspaceService;
    NamespaceScheduleService namespaceScheduleService;
    NamespaceService namespaceService;

    @Autowired
    public ApiController(KeysService keysService, DBWrapperCliService dbWrapperCliService, CliService cliService, KeyspaceService keyspaceService, NamespaceScheduleService namespaceScheduleService, NamespaceService namespaceService) {
        this.keysService = keysService;
        this.dbWrapperCliService = dbWrapperCliService;
        this.cliService = cliService;
        this.keyspaceService = keyspaceService;
        this.namespaceScheduleService = namespaceScheduleService;
        this.namespaceService = namespaceService;
    }

    /**
     * Endpoint for /keys command, returns keys and cursor depending on specified cursor, count and pattern in JSON format
     *
     * @param cursor
     * @param count
     * @param pattern
     * @param host
     * @param port
     * @return
     */
    @RequestMapping(value = "/keys", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String keys(@RequestParam(required = false, name = "cursor") Long cursor,
                       @RequestParam(required = false, name = "count") Long count,
                       @RequestParam(required = false, name = "pattern") String pattern,
                       @RequestParam(required = false, name = "host") String host,
                       @RequestParam(required = false, name = "port") Integer port,
                       @RequestParam(required = false, name = "db") Integer db) {
        LOGGER.info("GET /keys");
        return keysService.keysJson(cursor, count, pattern, host, port, db);
    }

    /**
     * Endpoint for /size command, returns amount of keys in database
     *
     * @param host
     * @param port
     * @return
     */
    @RequestMapping(value = "/size", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String size(@RequestParam(required = false, name = "host") String host,
                       @RequestParam(required = false, name = "port") Integer port,
                       @RequestParam(required = false, name = "db") Integer db) {
        LOGGER.info("GET /size");
        return dbWrapperCliService.wrapAndExecute(host, port, "DBSIZE", db);
    }

    @RequestMapping(value = "/keyspaces", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String keyspaces(@RequestParam(required = false, name = "host") String host,
                            @RequestParam(required = false, name = "port") Integer port) {
        LOGGER.info("GET /keyspaces");
        return keyspaceService.getKeySpacesJson(host, port);
    }

    @RequestMapping(value = "/scheduleNamespaces", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scheduleNamespaces(@RequestParam(required = false, name = "host") String host,
                             @RequestParam(required = false, name = "port") Integer port) {
        LOGGER.info("GET /scheduleNamespaces");
        namespaceScheduleService.scan(host, port);
        return "Done";
    }

    @RequestMapping(value = "/namespaces", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String namespaces(@RequestParam(required = false, name = "host") String host,
                             @RequestParam(required = false, name = "port") Integer port,
                             @RequestParam(required = false, name = "db") Integer db) {
        LOGGER.info("GET /namespaces");
        return namespaceService.getNamespaces(host, port, db);
    }
}
