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
        return store.get(cookie);
    }

    public void put(String cookie, State state) {
        store.put(cookie, state);
    }
}
