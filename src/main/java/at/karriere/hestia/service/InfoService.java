package at.karriere.hestia.service;

import at.karriere.hestia.component.InfoFilterComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoService {

    CliService cliService;
    InfoFilterComponent infoFilterComponent;

    @Autowired
    public InfoService(CliService cliService, InfoFilterComponent infoFilterComponent) {
        this.cliService = cliService;
        this.infoFilterComponent = infoFilterComponent;
    }

    public String getInfo(String host, Integer port) {

        //Get info from redis
        String info = cliService.executeCommand(host, port, "INFO");

        //return filtered info
        return infoFilterComponent.filter(info);
    }
}
