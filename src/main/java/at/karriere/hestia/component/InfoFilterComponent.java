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
     */
    public String filter(String info) {
        info = info.replaceAll("\r", "");
        String[] lines = info.split("\n");
        String filtered = "";
        String category = "";
        boolean categoryUsed = false;
        boolean inCategory = false;
        for (String line : lines) {
            switch (isInArray(line)) {
                case 1: //When property is detected
                    if (!categoryUsed && !category.equals("")) {
                        filtered += category + "\n";
                        categoryUsed = true;
                        category = "";
                    }
                    if (line.startsWith("uptime")) {
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
                    if (categoryUsed) {
                        filtered += "\n";
                    }
                    if (inCategory) {
                        inCategory = false;
                    }
                    break;
                case 4: //When category is detected
                    category = line;
                    categoryUsed = false;
                    break;
                default: //Default case
                    if (inCategory) {
                        filtered += line + "\n";
                    }
            }
        }
        return filtered;
    }

    /**
     * checks if line is in any filter array
     */
    private short isInArray(String line) {
        if (line.equals("\r") || line.equals("")) {
            return 3;
        }
        if (line.startsWith("#")) {
            for (String category : categories) {
                if (line.contains(category)) {
                    return 2;
                }
            }
            return 4;
        }
        for (String property : properties) {
            if (line.startsWith(property)) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * formats the uptime seconds
     */
    private String uptimeConverter(String line) {
        //Get value of key:value pair
        double fullSeconds = Double.valueOf(line.split(":")[1]);

        //Format time
        double days = fullSeconds / (60 * 60 * 24);
        double hours = (days % 1) * 24;
        double minutes = (hours % 1) * 60;
        double seconds = (minutes % 1) * 60;
        days -= days % 1;
        hours -= hours % 1;
        minutes -= minutes % 1;
        seconds -= seconds % 1;

        return "uptime(dd-HH-mm-ss):" + stringifyTime(days, hours, minutes, seconds) + "\n";
    }

    /**
     * Converts a single time value to a 2 digit String
     */
    private String stringifySingleTime(double time) {
        String result = "";
        if (time < 10) {
            result = "0";
        }
        return result + String.valueOf((int) time);
    }

    /**
     * Converts 4 time values to a dd-hh-mm-ss String
     */
    private String stringifyTime(double days, double hours, double minutes, double seconds) {
        String dd = stringifySingleTime(days);
        String hh = stringifySingleTime(hours);
        String mm = stringifySingleTime(minutes);
        String ss = stringifySingleTime(seconds);
        return dd + "-" + hh + "-" + mm + "-" + ss;
    }
}
