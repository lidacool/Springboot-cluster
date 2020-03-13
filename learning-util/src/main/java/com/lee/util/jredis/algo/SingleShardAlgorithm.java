package com.lee.util.jredis.algo;

import com.lee.util.jredis.ShardRedisPool;

import java.util.concurrent.atomic.AtomicInteger;

public class SingleShardAlgorithm extends ShardAlgorithm {

    final AtomicInteger next = new AtomicInteger();

    private int nextShardIndex() {
        if (this.pools.length == 1) {
            return 0;
        } else {
            int i = this.next.incrementAndGet();
            if (i < 0) {
                i = 0;
                this.next.set(i);
            }

            return i % this.pools.length;
        }
    }

    public SingleShardAlgorithm(ShardRedisPool[] pools) {
        super(pools);
    }

    @Override
    public int shardId(byte[] var1) {
        return 0;
    }

    @Override
    public ShardRedisPool shard(byte[] var1) {
        return this.pools[this.nextShardIndex()];
    }

    @Override
    public ShardRedisPool shard(int var1) {
        return this.pools[this.nextShardIndex()];
    }
}
