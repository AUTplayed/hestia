package at.karriere.hestia.component;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class JsonKeysConverterComponent {

    public String convert(String result) {
        //Split lines of String result into array
        String[] lines = result.split("\n");
        JSONObject response = new JSONObject();

        //When the array is empty return empty jsonobject
        if(lines.length < 2 && lines[0].equals("")) {
            return "{}";
        }

        //Put the cursor into the cursor field
        response.put("cursor", lines[0]);

        //If no keys are given, just return the cursor
        if(lines.length == 1) {
            return response.toString();
        }

        //Copy subarray and put it into a json array
        String[] keys = Arrays.copyOfRange(lines,1,lines.length);
        JSONArray jsonArray = new JSONArray(keys);

        //Put jsonarray into jsonobject and return it
        response.put("keys", jsonArray);
        return response.toString();
    }
}
