package com.lee.util.jredis.algo;

import com.lee.util.jredis.JRedisKey;
import com.lee.util.jredis.ShardRedisPool;

public class CageHashShardAlgorithm extends ShardAlgorithm {

    private static final int DEFAULT_USERS_PER_CAGE = 1024;
    private int usersPerCage;

    protected CageHashShardAlgorithm(ShardRedisPool[] pools) {
        this(pools, 1024);
    }

    protected CageHashShardAlgorithm(ShardRedisPool[] pools, int usersPerCage) {
        super(pools);
        this.usersPerCage = usersPerCage;
    }

    @Override
    public int shardId(byte[] key) {
        return shardIdByCage(getCage(key));
    }


    protected int getCage(byte[] key) {
        JRedisKey storageKey = JRedisKey.instance(key);
        int userId = (int)storageKey.getUserId();
        return this.getCage(userId);
    }

    public final int getCage(int userId) {
        assert userId >= 0;

        return userId / this.usersPerCage;
    }

    public int shardIdByCage(int cage) {
        return cage % this.pools.length;
    }

    @Override
    public ShardRedisPool shard(byte[] key) {
        return this.shardByCage(this.getCage(key));
    }

    public ShardRedisPool shardByCage(int cage) {
        return this.shard(this.shardIdByCage(cage));
    }

    @Override
    public ShardRedisPool shard(int var1) {
        return this.pools[var1];
    }
}
