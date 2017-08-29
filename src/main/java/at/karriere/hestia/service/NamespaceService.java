package at.karriere.hestia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NamespaceService {

    private DBWrapperCliService dbWrapperCliService;

    @Autowired
    public NamespaceService(DBWrapperCliService dbWrapperCliService) {
        this.dbWrapperCliService = dbWrapperCliService;
    }

    public String getNamespaces(String host, Integer port, Integer db) {
        String infoValue = dbWrapperCliService.wrapAndExecute(host, port, "GET info", db);
        if (infoValue.startsWith("ERR"))
            return "ERR";
        return infoValue;
    }
}
