package at.karriere.hestia.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServerConfigService {

    @Value("${servers:local:localhost:6379}")
    String servers;

    @Value("${redis.port:6379}")
    private Integer defaultPort;

    @Value("${redis.host:localhost}")
    private String defaultHostname;

    public String getServers() {
        String[] serverArray = servers.split(";");
        JSONArray jsonServerArray = new JSONArray();
        for(String server : serverArray) {
            String[] values = server.split(":");
            String name, host;
            Integer port = defaultPort;
            if(values.length > 0) {
                name = values[0];
                if(values.length > 1) {
                    host = values[1];
                    if(values.length > 2) {
                        port = Integer.valueOf(values[2]);
                    }
                } else {
                    host = defaultHostname;
                }
                JSONObject json = new JSONObject();
                json.put("name", name);
                json.put("host", host);
                json.put("port", port);
                jsonServerArray.put(json);
            }
        }
        return jsonServerArray.toString();
    }
}
