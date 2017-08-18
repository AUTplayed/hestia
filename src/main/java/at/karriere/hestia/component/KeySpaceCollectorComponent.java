package at.karriere.hestia.component;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class KeySpaceCollectorComponent {

    public Integer[] collect(String input) {
        String[] keyspaces = input.split("\n");
        Integer[] result = new Integer[keyspaces.length - 1];
        int counter = 0;
        //Skip keyspaces header
        keyspaces = Arrays.copyOfRange(keyspaces, 1, keyspaces.length);
        for(String keyspace : keyspaces) {
            String db = keyspace.split(":")[0];
            Integer dbNumber = Integer.valueOf(db.substring(2));
            result[counter++] = dbNumber;
        }

        return result;
    }
}
