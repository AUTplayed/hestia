package at.karriere.hestia.component;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class InfoFilterComponent {

    private final String[] properties = {"redis_version", "uptime_in_seconds", "tcp_port", "connected_clients", "role", "connected_slaves", "slave"};
    private final String[] categories = {"Memory", "Keyspace"};

    /**
     * filters all info into needed info
     * @param info
     * @return
     */
    public String filter(String info) {
        info = info.replaceAll("\r","");
        String[] lines = info.split("\n");
        String filtered = "";
        String category = "";
        boolean categoryUsed = false;
        boolean inCategory = false;
        for (String line : lines) {
            switch(isInArray(line)) {
                case 1: //When property is detected
                    if(!categoryUsed && !category.equals("")) {
                        filtered += category + "\n";
                        categoryUsed = true;
                        category = "";
                    }
                    if(line.startsWith("uptime")) {
                        filtered += uptimeConverter(line);
                    } else {
                        filtered += line + "\n";
                    }
                    break;
                case 2: //When matching category is detected
                    inCategory = true;
                    categoryUsed = true;
                    filtered += line + "\n";
                    break;
                case 3: //When newline is detected
                    if(categoryUsed) {
                        filtered += "\n";
                    }
                    if(inCategory) {
                        inCategory = false;
                    }
                    break;
                case 4: //When category is detected
                    category = line;
                    categoryUsed = false;
                    break;
                default: //Default case
                    if(inCategory) {
                        filtered += line + "\n";
                    }
            }
        }
        return filtered;
    }

    /**
     * checks if line is in any filter array
     * @param line
     * @return
     */
    private short isInArray(String line) {
        if(line.equals("\r") || line.equals("")) {
            return 3;
        }
        if(line.startsWith("#")) {
            for (String category : categories) {
                if(line.contains(category)) {
                    return 2;
                }
            }
            return 4;
        }
        for (String property : properties) {
            if(line.startsWith(property)) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * formats the uptime seconds
     * @param line
     * @return
     */
    private String uptimeConverter(String line) {
        //Get value of key:value pair
        long fullSeconds = Long.valueOf(line.split(":")[1]);

        //Format Time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("-hh-mm-ss");
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(fullSeconds, 0, ZoneOffset.UTC);
        int day = dateTime.getDayOfYear() - 1;
        String dd = day < 10 ? "0" + day : String.valueOf(day);
        return "uptime:" + dd + formatter.format(dateTime) + "\n";
    }
}
