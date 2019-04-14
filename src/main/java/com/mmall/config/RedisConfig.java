package com.mmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
//@ImportResource(locations = {"classpath:/applicationContext-spring-session.xml"})
public class RedisConfig {

/*    @Bean(name = "redisHttpSesionConfiguration")
    public RedisHttpSessionConfiguration getRedisHttpSessionConfiguration(){
        RedisHttpSessionConfiguration configuration = new RedisHttpSessionConfiguration();
        configuration.setMaxInactiveIntervalInSeconds(1800);

        return configuration;
    }*/

/*    @Bean(name = "defaultCookieSerializer")
    public DefaultCookieSerializer getDefaultCookieSerializer(){
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setDomainName(".mmall.com");
        serializer.setUseHttpOnlyCookie(true);
        serializer.setCookiePath("/");
        serializer.setCookieMaxAge(31536000);

        return serializer;
    }*/

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);

        return config;
    }

/*    @Bean(name = "jedisConnectionFactory")
    public JedisConnectionFactory getJedisConnectionFactory(@Qualifier("jedisPoolConfig")JedisPoolConfig jedisPoolConfig){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName("192.168.56.10");
        factory.setPort(6379);
        factory.setPoolConfig(jedisPoolConfig);

        return factory;
    }*/
}
