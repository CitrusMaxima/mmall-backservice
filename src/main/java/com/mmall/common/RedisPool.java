package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private static JedisPool jedisPool;     // Jedis连接池

    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));    // 最大连接数

    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));      // 在JedisPool中最大的空闲状态的Jedis实例的个数

    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.total", "2"));      // 在Jedispool中最小的空闲状态的Jedis实例的个数

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));    // 在borrow一个Jedis实例的时候，是否进行验证操作，如果赋值为true，则得到的Jedis实例肯定是可用的

    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "false"));    // 在return一个Jedis实例的时候，是否进行验证操作，如果赋值为true，则放回JedisPool的Jedis实例肯定是可用的

    private static String redisIp = PropertiesUtil.getProperty("redis1.ip");

    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    static {
        initPool();
    }

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true); // 连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时，默认为true

        jedisPool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static void returnResource(Jedis jedis) {
        jedisPool.returnResource(jedis);
    }

    public static void returnBrokenResource(Jedis jedis) {
        jedisPool.returnBrokenResource(jedis);
    }

}
