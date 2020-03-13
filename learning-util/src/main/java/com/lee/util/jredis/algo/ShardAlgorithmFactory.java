package com.lee.util.jredis.algo;

import com.lee.util.jredis.ShardRedisPool;

public class ShardAlgorithmFactory {

    private static boolean isSingleton;

    public ShardAlgorithmFactory() {
    }

    public static void setIsSingleton(boolean isSingleton) {
        isSingleton = isSingleton;
    }

    public static ShardAlgorithm newShardAlgorithm(ShardRedisPool[] pools) {
        if (pools.length != 1 && !isSingleton) {
            checkShardId(pools);
            return new ConsistentShardAlgorithm(pools);
        } else {
            return new SingleShardAlgorithm(pools);
        }
    }

    public static void checkShardId(ShardRedisPool[] pools) {
        for(int i = 0; i < pools.length; ++i) {
            if (pools[i].getShardId() != i) {
                throw new IllegalArgumentException("wrong shard id:" + i + "--" + pools[i].getShardId() + "--" + pools[i].getShardInfo());
            }
        }

    }
}
