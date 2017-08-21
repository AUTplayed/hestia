package at.karriere.hestia.service;

import at.karriere.hestia.component.KeySpaceCollectorComponent;
import at.karriere.hestia.component.NamespaceCollectorComponent;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class NamespaceScheduleService {

    final static Logger LOGGER = Logger.getLogger(NamespaceScheduleService.class);

    private NamespaceCollectorComponent namespaceCollectorComponent;
    private KeySpaceCollectorComponent keySpaceCollectorComponent;
    private KeysService keysService;
    private KeyspaceService keyspaceService;
    private DBWrapperCliService dbWrapperCliService;

    @Autowired
    public NamespaceScheduleService(NamespaceCollectorComponent namespaceCollectorComponent, KeySpaceCollectorComponent keySpaceCollectorComponent, KeysService keysService, KeyspaceService keyspaceService, DBWrapperCliService dbWrapperCliService) {
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
        String key = "info";
        String command = "GET " + key;
        String prevVal = dbWrapperCliService.wrapAndExecute(host, port, command, db);

        JSONObject info = null;
        Set<String> existingSet = new HashSet<>();
        if(!prevVal.equals("")) {

            //Previous info json (value)
            info = new JSONObject(prevVal);

            //Previous namespaces
            existingSet = info.keySet();
        }

        //New info json
        JSONObject newVal = new JSONObject();
        for (String namespace : map.keySet()) {
            JSONObject nameSpaceValue = new JSONObject();
            if(existingSet.contains(namespace)) {
                JSONObject prevNameSpaceValue = (JSONObject) info.get(namespace);
                String description = prevNameSpaceValue.get("description").toString();
                nameSpaceValue.put("description", description);
            } else {
                nameSpaceValue.put("description", "");
            }
            Long count = map.get(namespace);
            nameSpaceValue.put("count", count);
            newVal.put(namespace, nameSpaceValue);
        }

        String json = newVal.toString().replaceAll("\"","\\\\\"");
        command = "SET " + key + " \"" + json + "\"";
        dbWrapperCliService.wrapAndExecute(host, port, command, db);


    }

}
