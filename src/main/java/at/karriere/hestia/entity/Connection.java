package at.karriere.hestia.entity;

/**
 * Class for passing reference of host and port
 */
public class Connection {

    private Integer port;
    private String hostname;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Connection(String hostname, Integer port) {
        this.port = port;
        this.hostname = hostname;
    }
}
