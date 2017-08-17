package at.karriere.hestia.service;

import at.karriere.hestia.component.KeySpaceCollectorComponent;
import at.karriere.hestia.component.NamespaceCollectorComponent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class NamespaceService {

    final static Logger LOGGER = Logger.getLogger(NamespaceService.class);

    private NamespaceCollectorComponent namespaceCollectorComponent;
    private KeySpaceCollectorComponent keySpaceCollectorComponent;
    private KeysService keysService;
    private KeyspaceService keyspaceService;
    private DBWrapperCliService dbWrapperCliService;

    @Autowired
    public NamespaceService(NamespaceCollectorComponent namespaceCollectorComponent, KeySpaceCollectorComponent keySpaceCollectorComponent, KeysService keysService, KeyspaceService keyspaceService, DBWrapperCliService dbWrapperCliService) {
        this.namespaceCollectorComponent = namespaceCollectorComponent;
        this.keySpaceCollectorComponent = keySpaceCollectorComponent;
        this.keysService = keysService;
        this.keyspaceService = keyspaceService;
        this.dbWrapperCliService = dbWrapperCliService;
    }

    public void scan(String host, Integer port) {
        String result = keyspaceService.getKeySpaces(host, port);
        Integer[] keyspaces = keySpaceCollectorComponent.collect(result);
        for(Integer keyspace : keyspaces) {
            HashMap<String, Long> map = new HashMap<>();
            Long time = System.currentTimeMillis();
            scan(0L, 100000L, "*:*", host, port, keyspace, map);
            LOGGER.info("Scanning db" + keyspace + " took " + (System.currentTimeMillis() - time) / 1000 +"s");
            saveResults(map, host, port, keyspace);
        }
    }

    private void scan(Long cursor, Long count, String pattern, String host, Integer port, Integer db, HashMap<String, Long> map) {
        String keys = "";
        do {
            keys = keysService.keys(cursor, count, pattern, host, port, db);
            String[] keysArray = keys.split("\n");
            cursor = Long.valueOf(keysArray[0]);
            String[] keysCopy = Arrays.copyOfRange(keysArray, 1, keysArray.length);
            namespaceCollectorComponent.collect(keysCopy, map);
        } while (cursor != 0);
    }

    private void saveResults(HashMap<String, Long> map, String host, Integer port, Integer db) {
        for (String namespace : map.keySet()) {
            Long count = map.get(namespace);
            String key = "info:" + namespace;
            String command = "GET " + key;
            String value = String.valueOf(count);
            String prevValue = dbWrapperCliService.wrapAndExecute(host, port, command, db);
            String[] countAndName = prevValue.split(":");
            if(countAndName.length > 1) {
                value += ":" + countAndName[1];
            }
            command = "SET " + key + " " + value;
            dbWrapperCliService.wrapAndExecute(host, port, command, db);
        }

    }

}
