package at.karriere.services;

import at.karriere.components.OutputConverter;
import at.karriere.repositories.CliRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Protocol;
import redis.clients.util.RedisInputStream;
import redis.clients.util.RedisOutputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class CliService {

    CliRepository repository;
    OutputConverter outputConverter;
    final static Logger LOGGER = Logger.getLogger(CliService.class);

    @Autowired
    public CliService(CliRepository repository,OutputConverter outputConverter) {
        this.repository = repository;
        this.outputConverter = outputConverter;
    }

    public String executeCommand(String hostname,Integer port, String command, String... args){

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(System.getProperty("user.dir")+"/config/application.properties"));
        } catch (IOException e) {
            LOGGER.error("Could not load application properties");
        }

        hostname = prop.getProperty("redis.host");
        port = Integer.valueOf(prop.getProperty("redis.port"));

        if(!repository.connect(hostname, port)){
            return null;
        }else{
            RedisInputStream instream = repository.getInstream();
            RedisOutputStream outstream = repository.getOutstream();


            Protocol.Command cmd = Protocol.Command.valueOf(command.toUpperCase());
            byte[][] byteArgs = new byte[args.length][];
            for (int i = 0; i < args.length; i++) {
                byteArgs[i] = args[i].getBytes();
            }
            Protocol.sendCommand(outstream,cmd,byteArgs);
            try {
                outstream.flush();
            } catch (IOException e) {
                LOGGER.error("Failed to flush to redis",e);
            }
            Object result = Protocol.read(instream);

            repository.disconnect();
            return outputConverter.stringify(result);
        }


    }
}
