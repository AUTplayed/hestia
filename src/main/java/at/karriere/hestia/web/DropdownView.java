package at.karriere.hestia.web;

import at.karriere.hestia.entity.Connection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
@ViewScoped
public class DropdownView {

    private Map<String,Connection> data = new HashMap<>();
    private Map<String,String> servers = new HashMap<>();
    private String server;
    private String host;
    private Integer port;

    @PostConstruct
    public void init() {
        servers.put("sdev04","sdev04");
        data.put("sdev04",new Connection("10.10.10.152",6379));
        servers.put("sdev05","sdev05");
        data.put("sdev05",new Connection("10.10.10.153",6379));
    }

    public Map<String, Connection> getData() {
        return data;
    }

    public void setData(Map<String, Connection> data) {
        this.data = data;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Map<String, String> getServers() {
        return servers;
    }

    public void setServers(Map<String, String> servers) {
        this.servers = servers;
    }

    public void onChange() {
        if(!server.equals("")) {
            Connection connection = data.get(server);
            if(connection != null) {
                host = connection.getHostname();
                port = connection.getPort();
            }
        }
    }
}
