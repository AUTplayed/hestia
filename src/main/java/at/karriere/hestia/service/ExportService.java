package at.karriere.hestia.service;

import at.karriere.hestia.component.ExportFormatComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportService {

    DBWrapperCliService dbWrapperCliService;
    ExportFormatComponent exportFormatComponent;

    @Autowired
    public ExportService(DBWrapperCliService dbWrapperCliService, ExportFormatComponent exportFormatComponent) {
        this.dbWrapperCliService = dbWrapperCliService;
        this.exportFormatComponent = exportFormatComponent;
    }

    public String export(String host, Integer port, Integer db, String keys, String format) {
        String[] keyArray = keys.split("\n");
        if(keyArray.length < 1) {
            return "";
        }
        String command = "MGET";
        for(String key : keyArray) {
            command += " " + key;
        }
        String values = dbWrapperCliService.wrapAndExecute(host, port, command, db);
        String[] valueArray = values.split("\n");
        return exportFormatComponent.format(keyArray, valueArray, format);
    }
}
