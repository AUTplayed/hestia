package at.karriere.hestia.service;

import at.karriere.hestia.component.JsonKeysConverterComponent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeysService {
    final static Logger LOGGER = Logger.getLogger(KeysService.class);

    private JsonKeysConverterComponent jsonKeysConverterComponent;
    private DBWrapperCliService dbWrapperCliService;

    @Autowired
    public KeysService(JsonKeysConverterComponent jsonKeysConverterComponent, DBWrapperCliService dbWrapperCliService) {
        this.jsonKeysConverterComponent = jsonKeysConverterComponent;
        this.dbWrapperCliService = dbWrapperCliService;
    }

    public String keys(Long cursor, Long count, String pattern, String host, Integer port, Integer db) {
        return keys(cursor, count, pattern, host, port, db, true);
    }

    public String keys(Long cursor, Long count, String pattern, String host, Integer port, Integer db, boolean log) {
        if (cursor == null) {
            cursor = 0L;
        }
        if (count == null) {
            count = Long.valueOf(dbWrapperCliService.wrapAndExecute(host, port, "DBSIZE", db)) + 1;
        }
        if (pattern == null || pattern.equals("")) {
            pattern = "*";
        }
        String command = "SCAN " + cursor + " COUNT " + count + " MATCH " + pattern;
        return dbWrapperCliService.wrapAndExecute(host, port, command, db, log);
    }

    public String keysJson(Long cursor, Long count, String pattern, String host, Integer port, Integer db) {
        String result = keys(cursor, count, pattern, host, port, db);
        return jsonKeysConverterComponent.convert(result);
    }
}
