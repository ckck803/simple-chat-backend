package org.example.simplechat.common.redis.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import java.io.File;
import java.io.IOException;

@Configuration
@Profile("local")
@Slf4j
public class EmbeddedRedisConfig {

    private int port = 6379;
    private RedisServer redisServer;

    @PostConstruct
    public void startRedisServer() throws Exception {
        if (isM1Mac()) {
            log.info("Mac OS ARM Architecture");
            redisServer = new RedisServer(getRedisFileForArcMac(), port);
        } else {
            redisServer = new RedisServer(port);
        }

        redisServer.start();
    }

    @PreDestroy
    public void stopRedis(){
        if(redisServer != null){
            redisServer.stop();
        }
    }


    public boolean isM1Mac(){
        log.info("System OS: {}", System.getProperty("os.name"));
        log.info("System Architecture: {}", System.getProperty("os.arch"));

        if(!System.getProperty("os.name").toLowerCase().contains("mac")){
            return false;
        }

        return System.getProperty("os.arch").equals("aarch64");
    }


    public File getRedisFileForArcMac() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/binary/redis/redis-server");
        return classPathResource.getFile();
    }
}
