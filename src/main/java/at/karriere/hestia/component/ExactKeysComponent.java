package at.karriere.hestia.component;

import at.karriere.hestia.entity.State;
import at.karriere.hestia.entity.Vars;
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

    /**
     * Gets exact amount of keys specified
     */
    public String getKeys(State state, Long count, String pattern, String host, Integer port, Integer db) {
        String keys = "";
        double mean = state.getMean();
        long requestCount = count;

        //Take old mean if it exists
        if (mean != 0) {
            requestCount = (long) Math.ceil(count * mean);
        }
        keys = getKeysRec(state, requestCount, count, pattern, host, port, db);
        return keys;
    }

    /**
     * Recursively gets exact amount of keys requested
     */
    private String getKeysRec(State state, Long requestCount, Long remaining, String pattern, String host, Integer port, Integer db) {
        String rawKeys = keysService.keys(state.getCursor(), requestCount, pattern, host, port, db);
        String[] keysArray = parseKeys(state, rawKeys);
        double resCount = keysArray.length;
        if (resCount == 0) {

            //Increase next requestCount significantly if 0 keys are returned
            resCount = 0.1;
        } else {
            remaining -= (long) resCount;
        }
        state.addMean(requestCount / resCount);

        if (remaining < 0) {
            //Cut excess keys and return exact amount
            return addKeys(state, join(cutRest(state, keysArray, remaining)));
        }
        if (remaining > 0 && state.getCursor() != 0) {
            //Put keys into state for easy access and call recursion
            state.setKeys(addKeys(state, join(keysArray)));
            return getKeysRec(state, (long) Math.ceil(remaining * state.getMean()), remaining, pattern, host, port, db);
        }
        //When remaining = 0 just return the keys
        return addKeys(state, join(keysArray));
    }

    /**
     * Parse output returned from redis into cursor and keys
     */
    private String[] parseKeys(State state, String keys) {
        String[] keyArray = keys.split(Vars.DELIMITER);
        state.setCursor(Long.valueOf(keyArray[0]));
        return Arrays.copyOfRange(keyArray, 1, keyArray.length);
    }

    /**
     * Join string[] to String with \n delimiters
     */
    private String join(String[] array) {
        return String.join(Vars.DELIMITER, array);
    }

    /**
     * Cut excess keys from array and put them into the queue. Return requested amount.
     */
    private String[] cutRest(State state, String[] keysArray, Long remaining) {
        int limit = keysArray.length + Integer.valueOf(Long.toString(remaining));
        String[] perfectCut = Arrays.copyOfRange(keysArray, 0, limit);
        String[] rest = Arrays.copyOfRange(keysArray, limit, keysArray.length);
        state.addToQueue(rest);
        return perfectCut;
    }

    /**
     * Add together saved keys in state and keys passed, \n in between if none of them are empty
     */
    private String addKeys(State state, String add) {
        if (!state.getKeys().equals("")) {
            if (!add.equals("")) {
                return state.getKeys() + Vars.DELIMITER + add;
            }
            return state.getKeys();
        }
        return add;
    }
}
