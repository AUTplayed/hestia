package at.karriere.hestia.component;

import at.karriere.hestia.entity.State;
import at.karriere.hestia.service.KeysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ExactKeysComponent {

    private KeysService keysService;

    @Autowired
    public ExactKeysComponent(KeysService keysService) {
        this.keysService = keysService;
    }

    public String getKeys(State state, Long count, String pattern, String host, Integer port, Integer db) {
        String keys = "";
        if(pattern == null || pattern.equals("*")) {
            keys = keysService.keys(state.getCursor(), count, pattern, host, port, db);
            keys = join(parseKeys(state, keys));
        } else {
            double mean = state.getMean();
            long requestCount = count;
            if(mean != 0) {
                requestCount = (long)Math.ceil(count * mean);
            }
            keys = getKeysRec(state, requestCount, count, pattern, host, port, db);
        }
        return keys;
    }

    private String getKeysRec(State state, Long requestCount, Long remaining, String pattern, String host, Integer port, Integer db) {
        String rawKeys = keysService.keys(state.getCursor(), requestCount, pattern, host, port, db);
        String[] keysArray = parseKeys(state, rawKeys);
        double resCount = keysArray.length;
        if(resCount == 0) {
            resCount = 0.1;
        } else {
            remaining -= (long)resCount;
        }
        state.addMean(requestCount / resCount);

        if(remaining < 0) {
            return addKeys(state, join(cutRest(state, keysArray, remaining)));
        }
        if(remaining > 0 && state.getCursor() != 0) {
            state.setKeys(addKeys(state,join(keysArray)));
            return getKeysRec(state, (long)Math.ceil(remaining * state.getMean()), remaining, pattern, host, port, db);
        }
        return addKeys(state, join(keysArray));
    }

    private String[] parseKeys(State state, String keys) {
        String[] keyArray = keys.split("\n");
        state.setCursor(Long.valueOf(keyArray[0]));
        return Arrays.copyOfRange(keyArray, 1, keyArray.length);
    }

    private String join(String[] array) {
        return String.join("\n", array);
    }

    private String[] cutRest(State state, String[] keysArray, Long remaining) {
        int limit = keysArray.length + Integer.valueOf(Long.toString(remaining));
        String[] perfectCut = Arrays.copyOfRange(keysArray, 0, limit);
        String[] rest = Arrays.copyOfRange(keysArray, limit , keysArray.length);
        state.addToQueue(rest);
        return perfectCut;
    }

    private String addKeys(State state, String add) {
        if(!state.getKeys().equals("")) {
            if(!add.equals("")) {
                return state.getKeys() + "\n" + add;
            }
            return state.getKeys();
        }
        return add;
    }
}
