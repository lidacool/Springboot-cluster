package com.lee.util.jredis.algo;

import com.lee.util.jredis.ShardRedisPool;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentShardAlgorithm extends CageHashShardAlgorithm {

    private static final int DEFAULT_VIRTUAL_SHARDS_PER_SHARD = 40;

    private final SortedMap<Integer, ShardRedisPool> circle;
    private int virtualShardsPerShard;

    public ConsistentShardAlgorithm(ShardRedisPool[] pools) {
        this(40, pools);
    }

    public ConsistentShardAlgorithm(int virtualShardsPerShard, ShardRedisPool[] pools) {
        super(pools);
        this.circle = new TreeMap();
        this.virtualShardsPerShard = virtualShardsPerShard;

        for (int i = 0; i < pools.length; i++) {
            ShardRedisPool pool = pools[i];
            this.add(pool);
        }

    }

    private void add(ShardRedisPool pool) {
        StringBuilder sb = new StringBuilder("VS-");
        int len = sb.length();

        for (int i = 0; i < this.virtualShardsPerShard; ++i) {
            sb.setLength(len);
            sb.append(i);
            sb.append("-");
            sb.append(pool.getShardName());
            int hashCode = hash(sb.toString());
            this.circle.put(hashCode, pool);
        }

    }

    private static final int hash(String shardName) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes=messageDigest.digest(shardName.getBytes("UTF-8"));
            ByteBuffer byteBuffer=ByteBuffer.wrap(bytes);
            return Math.abs(byteBuffer.getInt());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

}
