package at.karriere.hestia.service;

import at.karriere.hestia.component.JsonKeySpaceConverterComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyspaceService {

    JsonKeySpaceConverterComponent jsonKeySpaceConverterComponent;
    CliService cliService;

    @Autowired
    public KeyspaceService(JsonKeySpaceConverterComponent jsonKeySpaceConverterComponent, CliService cliService) {
        this.jsonKeySpaceConverterComponent = jsonKeySpaceConverterComponent;
        this.cliService = cliService;
    }

    public String getKeySpaces(String host, Integer port) {

        String result = cliService.executeCommand(host,port,"info keyspace");
        return jsonKeySpaceConverterComponent.convert(result);
    }
}
