package at.karriere.hestia.service;

import at.karriere.hestia.component.CookieGenerateComponent;
import at.karriere.hestia.component.ExactKeysComponent;
import at.karriere.hestia.component.JsonKeysConverterComponent;
import at.karriere.hestia.entity.State;
import at.karriere.hestia.repository.StateStoreRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExactKeysService {

    final static Logger LOGGER = Logger.getLogger(ExactKeysService.class);

    KeysService keysService;
    StateStoreRepository stateStoreRepository;
    ExactKeysComponent exactKeysComponent;
    JsonKeysConverterComponent jsonKeysConverterComponent;
    CookieGenerateComponent cookieGenerateComponent;

    @Autowired
    public ExactKeysService(KeysService keysService, StateStoreRepository stateStoreRepository, ExactKeysComponent exactKeysComponent, JsonKeysConverterComponent jsonKeysConverterComponent, CookieGenerateComponent cookieGenerateComponent) {
        this.keysService = keysService;
        this.stateStoreRepository = stateStoreRepository;
        this.exactKeysComponent = exactKeysComponent;
        this.jsonKeysConverterComponent = jsonKeysConverterComponent;
        this.cookieGenerateComponent = cookieGenerateComponent;
    }

    public State keys(Long cursor, Long count, String pattern, String host, Integer port, Integer db, String cookie) {
        String keys = "";
        State state = getState(cookie, cursor, pattern, host, port, db);
        int sizeOfQueue = state.getSizeOfQueue();
        keys = addKeys(keys, getBuffer(state, count));
        count -= sizeOfQueue;
        if(count > 0) {
            keys = addKeys(keys, exactKeysComponent.getKeys(state, count, pattern, host, port, db));
        }
        state.setKeys(keys);
        LOGGER.info(state.getSizeOfQueue() + " key(s) in queue after request");
        return state;
    }

    public String keysJson(Long cursor, Long count, String pattern, String host, Integer port, Integer db, String cookie) {
        State state = keys(cursor, count, pattern, host, port, db, cookie);
        String keys = state.getCursor() + "\n" + state.getKeys();
        return jsonKeysConverterComponent.convert(keys, state.getCookie());
    }

    private String getBuffer(State state, Long count) {
        return state.getFromQueue(count);
    }

    private State getState(String cookie, Long cursor, String pattern, String host, Integer port, Integer db) {
        if(cookie == null) {
            cookie = cookieGenerateComponent.generate();
        }
        State state = stateStoreRepository.get(cookie);
        state.setKeys("");
        state.setCookie(cookie);
        if(!state.getCursor().equals(cursor)) {
            state.clearQueue();
        }
        if(!state.getPattern().equals(pattern) || !state.isSameConnection(host, port, db)) {
            state.clear();
        }
        state.setPattern(pattern);
        state.setCursor(cursor);
        return state;
    }
    private String addKeys(String add1, String add2) {
        if(!add1.equals("")) {
            return add1 + "\n" + add2;
        } else {
            return add2;
        }
    }


}
