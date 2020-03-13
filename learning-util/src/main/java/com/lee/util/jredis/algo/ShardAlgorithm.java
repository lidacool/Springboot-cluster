package com.lee.util.jredis.algo;

import com.lee.util.jredis.ShardRedisPool;

import java.util.Arrays;

public abstract class ShardAlgorithm {

    protected final ShardRedisPool[] pools;

    public ShardAlgorithm(ShardRedisPool[] pools) {
        this.pools = pools;
    }

    public abstract int shardId(byte[] var1);

    public abstract ShardRedisPool shard(byte[] var1);

    public abstract ShardRedisPool shard(int var1);

    public ShardRedisPool[] getShardPools() {
        return (ShardRedisPool[]) Arrays.copyOf(this.pools, this.pools.length);
    }

}
