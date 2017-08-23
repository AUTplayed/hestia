package at.karriere.hestia.component;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ExportFormatComponent {


    public String format(String[] keyArray, String[] valueArray, String format) {
        switch (format) {
            case "csv":
                return formatCsv(keyArray, valueArray);
            case "json":
                return formatJson(keyArray, valueArray);
        }
        return "";
    }

    public String formatCsv(String[] keyArray, String[] valueArray) {
        String output = "sep=,\nkey,value";
        for (int i = 0; i < keyArray.length; i++) {
            output += "\n";
            output += keyArray[i];
            output += ",";
            output += valueArray[i];
        }
        return output;
    }

    public String formatJson(String[] keyArray, String[] valueArray) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < keyArray.length; i++) {
            JSONObject json = new JSONObject();
            json.put("key", keyArray[i]);
            json.put("value", valueArray[i]);
            jsonArray.put(json);
        }
        return jsonArray.toString();
    }
}
