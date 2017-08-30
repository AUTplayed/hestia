package at.karriere.hestia.component;

import at.karriere.hestia.entity.Progress;
import at.karriere.hestia.service.DBWrapperCliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScanProgressComponent {

    DBWrapperCliService dbWrapperCliService;

    @Autowired
    public ScanProgressComponent(DBWrapperCliService dbWrapperCliService) {
        this.dbWrapperCliService = dbWrapperCliService;
    }

    public Progress start(String host, Integer port, Integer db) {
        String result = dbWrapperCliService.wrapAndExecute(host, port, "DBSIZE", db);
        Long dbsize = Long.valueOf(result);
        return new Progress(dbsize, db, host + ":" + port);
    }

}
