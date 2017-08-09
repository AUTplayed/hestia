package at.karriere.hestia.service;

import at.karriere.hestia.component.JsonKeysConverterComponent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeysService {
    final static Logger LOGGER = Logger.getLogger(KeysService.class);

    CliService cliService;
    JsonKeysConverterComponent jsonKeysConverterComponent;

    @Autowired
    public KeysService(CliService cliService, JsonKeysConverterComponent jsonKeysConverterComponent) {
        this.cliService = cliService;
        this.jsonKeysConverterComponent = jsonKeysConverterComponent;
    }

    public String keys(Long cursor, Long count, String pattern, String host, Integer port) {
        if (cursor == null) {
            cursor = 0L;
        }
        if (count == null) {
            count = Long.valueOf(cliService.executeCommand(host, port,"DBSIZE")) + 1;
        }
        if (pattern == null || pattern.equals("")) {
            pattern = "*";
        }
        String command = "SCAN "+cursor+" COUNT "+count+" MATCH "+pattern;
        String result = cliService.executeCommand(host, port, command);
        return jsonKeysConverterComponent.convert(result);
    }
}
