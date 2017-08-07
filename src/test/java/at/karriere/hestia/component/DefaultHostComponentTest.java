package at.karriere.hestia.component;

import at.karriere.hestia.entity.Connection;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;


public class DefaultHostComponentTest {

    private DefaultHostComponent defaultHostComponent = new DefaultHostComponent();
    private String hostname = "localhost";
    private Integer port = 1234;
    private String defaultHostname = "default";
    private Integer defaultPort = 1111;

    @Before
    public void prepare() {
        defaultHostComponent.setDefault(defaultHostname,defaultPort);
    }

    @Test
    public void testNoChanges() {
        Connection connection = new Connection(hostname,port);
        defaultHostComponent.check(connection);
        Assertions.assertThat(connection.getHostname()).as("hostname should be left alone").isEqualTo(hostname);
        Assertions.assertThat(connection.getPort()).as("port should be left alone").isEqualTo(port);
    }

    @Test
    public void testHostname() {
        Connection connection = new Connection(null,port);
        defaultHostComponent.check(connection);
        Assertions.assertThat(connection.getHostname()).as("hostname should be set to default").isEqualTo(defaultHostname);
        Assertions.assertThat(connection.getPort()).as("port should be left alone").isEqualTo(port);
    }

    @Test
    public void testPort() {
        Connection connection = new Connection(hostname,null);
        defaultHostComponent.check(connection);
        Assertions.assertThat(connection.getHostname()).as("hostname should be left alone").isEqualTo(hostname);
        Assertions.assertThat(connection.getPort()).as("port should be set to default").isEqualTo(defaultPort);
    }

    @Test
    public void testHostnameAndPort() {
        Connection connection = new Connection(null,null);
        defaultHostComponent.check(connection);
        Assertions.assertThat(connection.getHostname()).as("hostname should be set to default").isEqualTo(defaultHostname);
        Assertions.assertThat(connection.getPort()).as("port should be be set to default").isEqualTo(defaultPort);
    }
}

