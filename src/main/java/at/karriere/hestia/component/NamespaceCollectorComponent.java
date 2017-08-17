package at.karriere.hestia.component;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class NamespaceCollectorComponent {


    public void collect(String[] keys, HashMap<String, Long> map) {
        for (String key : keys) {
            String[] namespace = key.split(":");
            if(namespace.length > 1) {
                key = namespace[0];
                if(map.containsKey(key)) {
                    Long count = map.get(key);
                    map.replace(key, count + 1);
                } else {
                    map.put(key, 1L);
                }
            }
        }
    }
}
