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
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
public class ApiController {
    final static Logger LOGGER = Logger.getLogger(ApiController.class);

    KeysService keysService;
    KeyspaceService keyspaceService;
    NamespaceScheduleService namespaceScheduleService;
    NamespaceService namespaceService;
    InfoService infoService;
    ExactKeysService exactKeysService;
    ExportService exportService;

    @Autowired
    public ApiController(KeysService keysService, KeyspaceService keyspaceService, NamespaceScheduleService namespaceScheduleService, NamespaceService namespaceService, InfoService infoService, ExportService exportService, ExactKeysService exactKeysService) {
        this.keysService = keysService;
        this.keyspaceService = keyspaceService;
        this.namespaceScheduleService = namespaceScheduleService;
        this.namespaceService = namespaceService;
        this.infoService = infoService;
        this.exactKeysService = exactKeysService;
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

        //Get result and cookie
        String result =  exactKeysService.keysJson(cursor, count, pattern, host, port, db, cookie);
        JSONObject json = new JSONObject(result);
        String resCookie = json.getString("cookie");

        //Set cookie on client side via http set cookie header
        response.addCookie(new Cookie("state", resCookie));
        return result;
    }
    
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public String export(@RequestParam(required = false, name = "host") String host,
                         @RequestParam(required = false, name = "port") Integer port,
                         @RequestParam(required = false, name = "db") Integer db,
                         @RequestParam(required = false, name = "format") String format,
                         @RequestBody String keys) throws UnsupportedEncodingException {
        LOGGER.info("GET /export");
        keys = URLDecoder.decode(keys, "utf-8");
        return exportService.export(host, port, db, keys, format);
    }

}
