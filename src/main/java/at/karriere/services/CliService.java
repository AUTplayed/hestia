package at.karriere.services;

import at.karriere.repositories.CliRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Protocol;
import redis.clients.util.RedisInputStream;
import redis.clients.util.RedisOutputStream;

import java.io.IOException;

@Service
public class CliService {

    CliRepository repository;
    final static Logger LOGGER = Logger.getLogger(CliService.class);

    @Autowired
    public CliService(CliRepository repository) {
        this.repository = repository;
    }

    public Object executeCommand(String hostname,int port, String command, String... args){

        if(!repository.connect(hostname, port)){
            return null;
        }else{
            RedisInputStream instream = repository.getInstream();
            RedisOutputStream outstream = repository.getOutstream();


            Protocol.Command cmd = Protocol.Command.valueOf(command);
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

            return result;
        }


    }
}
