package at.karriere.hestia.controller;

import at.karriere.hestia.service.*;
import org.apache.log4j.Logger;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiController {
    final static Logger LOGGER = Logger.getLogger(ApiController.class);

    KeysService keysService;
    KeyspaceService keyspaceService;
    NamespaceScheduleService namespaceScheduleService;
    NamespaceService namespaceService;
    InfoService infoService;
    ExportService exportService;

    @Autowired
    public ApiController(KeysService keysService, KeyspaceService keyspaceService, NamespaceScheduleService namespaceScheduleService, NamespaceService namespaceService, InfoService infoService, ExportService exportService) {
        this.keysService = keysService;
        this.keyspaceService = keyspaceService;
        this.namespaceScheduleService = namespaceScheduleService;
        this.namespaceService = namespaceService;
        this.infoService = infoService;
        this.exportService = exportService;
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

    @RequestMapping(value = "/namespaces", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String namespaces(@RequestParam(required = false, name = "host") String host,
                             @RequestParam(required = false, name = "port") Integer port,
                             @RequestParam(required = false, name = "db") Integer db) {
        LOGGER.info("GET /namespaces");
        return namespaceService.getNamespaces(host, port, db);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String info(@RequestParam(required = false, name = "host") String host,
                       @RequestParam(required = false, name = "port") Integer port) {
        LOGGER.info("GET /info");
        return infoService.getInfo(host, port);
    }

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public String export(@RequestParam(required = false, name = "host") String host,
                         @RequestParam(required = false, name = "port") Integer port,
                         @RequestParam(required = false, name = "db") Integer db,
                         @RequestParam(required = false, name = "format", defaultValue = "csv") String format,
                         @RequestBody String keys) {
        LOGGER.info("GET /export");
        return exportService.export(host, port, db, keys, format);
    }

}
