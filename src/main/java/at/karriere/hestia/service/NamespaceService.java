package at.karriere.hestia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NamespaceService {

    DBWrapperCliService dbWrapperCliService;

    @Autowired
    public NamespaceService(DBWrapperCliService dbWrapperCliService) {
        this.dbWrapperCliService = dbWrapperCliService;
    }

    public String getNamespaces(String host, Integer port, Integer db) {
        String infoKeys = dbWrapperCliService.wrapAndExecute(host, port, "KEYS info:*", db);
        if(infoKeys.startsWith("ERR"))
            return "ERR";
        String[] keys = infoKeys.split("\n");
        String result = "";
        for(String key : keys) {
            String value = dbWrapperCliService.wrapAndExecute(host, port, "GET " + key, db);
            result += key.split(":")[1] + ":";
            result += value + "\n";
        }
        return result;
    }
}
