package at.karriere.hestia.service;

import at.karriere.hestia.component.DefaultHostComponent;
import at.karriere.hestia.component.OutputConverterComponent;
import at.karriere.hestia.component.SplitCommandComponent;
import at.karriere.hestia.entity.CommandContainer;
import at.karriere.hestia.entity.Connection;
import at.karriere.hestia.repository.CliRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.util.RedisInputStream;
import redis.clients.util.RedisOutputStream;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class CliService {

    private CliRepository repository;
    private OutputConverterComponent outputConverterComponent;
    private DefaultHostComponent defaultHostComponent;
    private SplitCommandComponent splitCommandComponent;
    final static Logger LOGGER = Logger.getLogger(CliService.class);

    @Value("${redis.host:localhost}")
    private String defaultHostname;
    @Value("${redis.port:6379}")
    private Integer defaultPort;


    @Autowired
    public CliService(CliRepository repository, OutputConverterComponent outputConverterComponent, DefaultHostComponent defaultHostComponent, SplitCommandComponent splitCommandComponent) {
        this.repository = repository;
        this.outputConverterComponent = outputConverterComponent;
        this.defaultHostComponent = defaultHostComponent;
        this.splitCommandComponent = splitCommandComponent;

    }

    @PostConstruct
    public void init() {
        defaultHostComponent.setDefault(defaultHostname,defaultPort);
    }

    /**
     * Executes a specified command on specified host and port redis server, returning results in String form
     * @param hostname
     * @param port
     * @param command
     * @return
     */
    public String executeCommand(String hostname,Integer port, String command) {

        //Split commandString into command and args
        CommandContainer commandContainer = splitCommandComponent.split(command);

        command = commandContainer.getCommand();
        String [] args = commandContainer.getArgs();

        Connection connection = new Connection(hostname,port);
        defaultHostComponent.check(connection);

        //Connect to redis server socket
        if (!repository.connect(connection.getHostname(), connection.getPort())) {
            return "ERR failed to connect to specified hostname and port";
        } else {

            //Parse args to command enum
            Protocol.Command cmd = null;
            try {
                 cmd = Protocol.Command.valueOf(command.toUpperCase());
            } catch (IllegalArgumentException e) {
                LOGGER.error("Failed to parse command");
                return "ERR illegal command '"+command.toLowerCase()+"'";
            }

            //Parse args to byte array
            byte[][] byteArgs = new byte[args.length][];
            for (int i = 0; i < args.length; i++) {
                byteArgs[i] = args[i].getBytes();
            }

            //Send command
            try {
                repository.sendToRedis(cmd,byteArgs);
            } catch (IOException e) {
                LOGGER.error("Failed to flush to redis",e);
            }

            //Receive result
            Object result = null;
            try {
                result = repository.readResult();
            } catch (JedisDataException e) {
                LOGGER.error("Failed to execute command",e);
                return e.getMessage();
            }

            //Stringify and return result
            repository.disconnect();
            return outputConverterComponent.stringify(result);
        }


    }
}
