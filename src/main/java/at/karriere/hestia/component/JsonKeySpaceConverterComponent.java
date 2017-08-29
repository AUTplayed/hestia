package at.karriere.hestia.component;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class JsonKeySpaceConverterComponent {
    public String convert(String result) {
        //Split lines of String result into array
        String[] lines = result.split("\n");

        //When the array is empty return empty jsonarray
        if (lines.length < 1) {
            return "[]";
        }

        //Copy subarray and put it into a json array
        String[] keyspaces = Arrays.copyOfRange(lines, 1, lines.length);
        for (int i = 0; i < keyspaces.length; i++) {
            keyspaces[i] = keyspaces[i].split(",")[0];
        }
        JSONArray jsonArray = new JSONArray(keyspaces);

        //Return Array as String
        return jsonArray.toString();
    }
}
