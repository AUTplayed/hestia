package at.karriere.repositories;

import at.karriere.controller.CliController;
import org.apache.log4j.Logger;
import redis.clients.jedis.*;
import redis.clients.util.RedisOutputStream;

@org.springframework.stereotype.Repository
public class Repository {
    final static Logger LOGGER = Logger.getLogger(CliController.class);
    private Jedis jedis;

    public void connect(String host,int port){
        jedis = new Jedis(host,port);
        LOGGER.info("Connected to redis server: "+host+":"+port);
        LOGGER.info("Connection status: PING---"+jedis.ping());

        jedis.monitor(new JedisMonitor() {
            @Override
            public void onCommand(String command) {
                LOGGER.info("Executed redis command: "+command);
            }
        });


    }



}
