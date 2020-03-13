package com.lee.util.jredis;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

public class ShardRedisPool {

    private final GenericObjectPool<Jedis> internalPool;
    private final RedisShardInfo shard;

    public ShardRedisPool(GenericObjectPoolConfig poolConfig, RedisShardInfo shard) {
        this.internalPool = new GenericObjectPool(new ShardedRedisFactory(shard), poolConfig);
        this.shard = shard;
    }

    private static class ShardedRedisFactory extends BasePooledObjectFactory<Jedis> {

        private final RedisShardInfo shardInfo;

        public ShardedRedisFactory(RedisShardInfo shardInfo) {
            this.shardInfo = shardInfo;
        }

        @Override
        public Jedis create() throws Exception {
            Jedis jedis = new Jedis(this.shardInfo.getMasterHost(), this.shardInfo.getMasterPort(), this.shardInfo.getTimeout());
            if (this.shardInfo.hasMasterPassword()) {
                jedis.auth(this.shardInfo.getMasterPassword());
            }
            return jedis;
        }


        @Override
        public PooledObject<Jedis> wrap(Jedis jedis) {
            return new DefaultPooledObject<>(jedis);
        }

        @Override
        public void destroyObject(PooledObject<Jedis> p) {
            Jedis jedis = p.getObject();

            jedis.quit();

            jedis.disconnect();
        }

        @Override
        public boolean validateObject(PooledObject<Jedis> p) {
            Jedis jedis = p.getObject();
            return jedis.ping().equals("PONG");
        }
    }

    public Jedis getResource() {
        try {
            Jedis jedis = (Jedis)this.internalPool.borrowObject();
            return jedis;
        } catch (Exception var2) {
            throw new JedisConnectionException("Could not get a resource from the pool", var2);
        }
    }

    public void returnResource(Jedis resource) {
        try {
            this.internalPool.returnObject(resource);
        } catch (Exception var3) {
            throw new JedisException("Could not return the resource to the pool", var3);
        }
    }

    public void returnBrokenResource(Jedis resource) {
        try {
            this.internalPool.invalidateObject(resource);
        } catch (Exception var3) {
            throw new JedisException("Could not return the resource to the pool", var3);
        }
    }

    public void destroy() {
        try {
            this.internalPool.close();
        } catch (Exception var2) {
            throw new JedisException("Could not destroy the pool", var2);
        }
    }


    public int getNumActive() {
        return this.internalPool.getNumActive();
    }

    public int getNumIdle() {
        return this.internalPool.getNumIdle();
    }

    public String getShardInfo() {
        return this.shard.toString();
    }

    public String getShardName() {
        return this.shard.getShardName();
    }

    public int getShardId() {
        return this.shard.getShardId();
    }
}
