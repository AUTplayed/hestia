package at.karriere.hestia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExactKeysService {

    KeysService keysService;

    @Autowired
    public ExactKeysService(KeysService keysService) {
        this.keysService = keysService;
    }

    public String keys(Long cursor, Long count, String pattern, String host, Integer port, Integer db, String cookie) {
        return null;
    }

    public String keysJson() {
        return null;
    }

    public String getBuffer() {
        return null;
    }
}
