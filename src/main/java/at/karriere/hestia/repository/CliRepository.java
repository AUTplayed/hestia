package at.karriere.hestia.repository;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.util.RedisInputStream;
import redis.clients.util.RedisOutputStream;

import javax.xml.ws.BindingType;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

@org.springframework.stereotype.Repository
@Scope("prototype")
public class CliRepository {

    final static Logger LOGGER = Logger.getLogger(CliRepository.class);

    private Socket socket;
    private RedisInputStream instream;
    private RedisOutputStream outstream;

    /**
     * Connect to specified host and port via socket and setup streams
     * @param hostname
     * @param port
     * @return
     */
    public boolean connect(String hostname,int port) {
        socket = new Socket();
        try {
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            socket.connect(new InetSocketAddress(hostname, port),3000);
            outstream = new RedisOutputStream(socket.getOutputStream());
            instream = new RedisInputStream(socket.getInputStream());
        } catch (SocketException e) {
            LOGGER.error("Error while setting socket settings",e);
            return false;
        } catch (IOException e) {
            LOGGER.error("Error while connecting to redis server via socket",e);
            return false;
        }
        return true;
    }

    private boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed() && socket.isConnected()
                && !socket.isInputShutdown() && !socket.isOutputShutdown();
    }

    public void sendToRedis(Protocol.Command command, byte[]... args) throws IOException {
        if (isConnected()) {
            Protocol.sendCommand(outstream,command,args);
            outstream.flush();
        } else {
            throw new IOException("Connection not open");
        }
    }

    public Object readResult() throws JedisDataException {
        if (isConnected()) {
            return Protocol.read(instream);
        } else {
            throw new JedisDataException("Connection not open");
        }
    }

    /**
     * Disconnect from connected socket and closing streams
     */
    public void disconnect(){
        try {
            if (instream != null) {
                instream.close();
            }
            if (outstream != null) {
                outstream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to close socket/streams",e);
        }
    }

}
