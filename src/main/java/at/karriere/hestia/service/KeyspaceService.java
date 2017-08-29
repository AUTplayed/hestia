package at.karriere.hestia.service;

import at.karriere.hestia.component.JsonKeySpaceConverterComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyspaceService {

    private JsonKeySpaceConverterComponent jsonKeySpaceConverterComponent;
    private CliService cliService;

    @Autowired
    public KeyspaceService(JsonKeySpaceConverterComponent jsonKeySpaceConverterComponent, CliService cliService) {
        this.jsonKeySpaceConverterComponent = jsonKeySpaceConverterComponent;
        this.cliService = cliService;
    }

    public String getKeySpaces(String host, Integer port) {
        return cliService.executeCommand(host, port, "info keyspace");
    }

    public String getKeySpacesJson(String host, Integer port) {
        String result = getKeySpaces(host, port);
        return jsonKeySpaceConverterComponent.convert(result);
    }
}
