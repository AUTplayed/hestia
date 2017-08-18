package at.karriere.hestia.component;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
public class NamespaceCollectorComponent {


    public void collect(String[] keys, HashMap<String, Long> map) {
        List<String> list = Arrays.asList(keys);
        list.parallelStream().forEach((key) -> {
            key = key.split(":")[0];
            map.putIfAbsent(key, 0L);
            Long count = map.get(key);
            map.replace(key, count + 1);
        });
        /*for (String key : keys) {
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
        }*/
    }
}
