package at.karriere.hestia.repository;

import at.karriere.hestia.entity.State;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class StateStoreRepository {

    private HashMap<String, State> store;

    public StateStoreRepository() {
        store = new HashMap<>();
    }

    public State get(String cookie) {
        State state = store.get(cookie);
        if(state == null) {
            state = new State();
            put(cookie, state);
        }
        return state;
    }

    private void put(String cookie, State state) {
        store.put(cookie, state);
    }
}
