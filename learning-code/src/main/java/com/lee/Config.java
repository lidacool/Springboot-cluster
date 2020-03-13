package com.lee;

import com.lee.util.jredis.JRedis;
import com.lee.util.jredis.JRedisFactory;
import com.lee.util.log.Logging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${learning.redis.master}")
    private String redis_master;

    @Value("${learning.redis.slave}")
    private String redis_slave;

    @Bean("jRedis")
    public JRedis getJRedis() {
        Logging.info(redis_master);
        Logging.info(redis_slave);
        return JRedisFactory.createJredis(redis_master, redis_slave);
    }

}
