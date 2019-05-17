/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srmasset.thcepdetails.pool;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author lprates
 */

@Configuration
public class PoolRedis {
    
    
        static final JedisPoolConfig poolConfig = buildPoolConfig();
        static JedisPool jedisPool = new JedisPool(poolConfig, "localhost");    
    
        private static JedisPoolConfig buildPoolConfig() {
            final JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(128);
            poolConfig.setMaxIdle(128);
            poolConfig.setMinIdle(16);
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            poolConfig.setTestWhileIdle(true);
            poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
            poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
            poolConfig.setNumTestsPerEvictionRun(3);
            poolConfig.setBlockWhenExhausted(true);
            return poolConfig;
        }    

        @Bean
        public static JedisPool getJedisPool(){
            return jedisPool ; 
        }
        
    
    
}
