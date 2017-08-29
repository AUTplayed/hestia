package at.karriere.hestia.repository;

import at.karriere.hestia.entity.State;
import org.apache.commons.collections4.map.LRUMap;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class StateStoreRepository {

    //private HashMap<String, State> store;
    private LRUMap<String, State> store;

    public StateStoreRepository() {
        store = new LRUMap<>(100);
    }

    public State get(String cookie) {
        State state = store.get(cookie);
        if (state == null) {
            state = new State();
            put(cookie, state);
        }
        return state;
    }

    private void put(String cookie, State state) {
        store.put(cookie, state);
    }
}
