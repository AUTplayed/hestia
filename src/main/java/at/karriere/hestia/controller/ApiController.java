package at.karriere.hestia.controller;

import at.karriere.hestia.service.CliService;
import at.karriere.hestia.service.DBWrapperCliService;
import at.karriere.hestia.service.KeysService;
import at.karriere.hestia.service.KeyspaceService;
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

    @Autowired
    public ApiController(KeysService keysService, CliService cliService, DBWrapperCliService dbWrapperCliService, KeyspaceService keyspaceService) {
        this.keysService = keysService;
        this.cliService = cliService;
        this.dbWrapperCliService = dbWrapperCliService;
        this.keyspaceService = keyspaceService;
    }

    /**
     * Endpoint for /keys command, returns keys and cursor depending on specified cursor, count and pattern in JSON format
     * @param cursor
     * @param count
     * @param pattern
     * @param host
     * @param port
     * @return
     */
    @RequestMapping(value = "/keys",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String keys(@RequestParam(required = false, name = "cursor") Long cursor,
                       @RequestParam(required = false, name = "count") Long count,
                       @RequestParam(required = false, name = "pattern") String pattern,
                       @RequestParam(required = false, name = "host") String host,
                       @RequestParam(required = false, name = "port") Integer port,
                       @RequestParam(required = false, name = "db") Integer db) {
        LOGGER.info("GET /keys");
        return keysService.keys(cursor, count, pattern, host, port, db);
    }

    /**
     * Endpoint for /size command, returns amount of keys in database
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
        return keyspaceService.getKeySpaces(host, port);
    }

}
