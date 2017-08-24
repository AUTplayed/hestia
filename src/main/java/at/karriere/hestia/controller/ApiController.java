package at.karriere.hestia.controller;

import at.karriere.hestia.service.*;
import org.apache.log4j.Logger;
import org.bouncycastle.cert.ocsp.Req;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ApiController {
    final static Logger LOGGER = Logger.getLogger(ApiController.class);

    KeysService keysService;
    KeyspaceService keyspaceService;
    NamespaceScheduleService namespaceScheduleService;
    NamespaceService namespaceService;
    InfoService infoService;
    ExactKeysService exactKeysService;

    @Autowired
    public ApiController(KeysService keysService, KeyspaceService keyspaceService, NamespaceScheduleService namespaceScheduleService, NamespaceService namespaceService, InfoService infoService, ExactKeysService exactKeysService) {
        this.keysService = keysService;
        this.keyspaceService = keyspaceService;
        this.namespaceScheduleService = namespaceScheduleService;
        this.namespaceService = namespaceService;
        this.infoService = infoService;
        this.exactKeysService = exactKeysService;
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

    @RequestMapping(value = "/exactKeys", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String exactKeys(@RequestParam(required = false, name = "cursor") Long cursor,
                            @RequestParam(required = false, name = "count") Long count,
                            @RequestParam(required = false, name = "pattern") String pattern,
                            @RequestParam(required = false, name = "host") String host,
                            @RequestParam(required = false, name = "port") Integer port,
                            @RequestParam(required = false, name = "db") Integer db,
                            @CookieValue(value = "state", required = false) String cookie,
                            HttpServletResponse response) {
        LOGGER.info("GET /exactKeys");
        String result =  exactKeysService.keysJson(cursor, count, pattern, host, port, db, cookie);
        JSONObject json = new JSONObject(result);
        String resCookie = json.getString("cookie");
        response.addCookie(new Cookie("state", resCookie));
        return result;
    }

}
