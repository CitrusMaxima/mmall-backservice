package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {

    private static ShardedJedisPool jedisPool;     // Sharded Jedis连接池

    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));    // 最大连接数

    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));      // 在JedisPool中最大的空闲状态的Jedis实例的个数

    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.total", "2"));      // 在Jedispool中最小的空闲状态的Jedis实例的个数
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));    // 在borrow一个Jedis实例的时候，是否进行验证操作，如果赋值为true，则得到的Jedis实例肯定是可用的

    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "false"));    // 在return一个Jedis实例的时候，是否进行验证操作，如果赋值为true，则放回JedisPool的Jedis实例肯定是可用的

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");

    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");

    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

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

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip, redis1Port, 1000 * 2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip, redis2Port, 1000 * 2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>();
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        jedisPool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);

    }

    public static ShardedJedis getJedis() {
        return jedisPool.getResource();
    }

    public static void returnResource(ShardedJedis jedis) {
        jedisPool.returnResource(jedis);
    }

    public static void returnBrokenResource(ShardedJedis jedis) {
        jedisPool.returnBrokenResource(jedis);
    }

}
