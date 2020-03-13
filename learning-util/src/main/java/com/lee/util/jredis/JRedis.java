package com.lee.util.jredis;

import com.lee.util.jredis.pipeline.PipelineCommand;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.util.SafeEncoder;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface JRedis {
    Long expireAt(byte[] var1, long var2);

    Set<String> keys(String var1);

    List<byte[]> sort(byte[] var1);

    List<byte[]> sort(byte[] var1, SortingParams var2);

    Long ttl(byte[] var1);

    String type(byte[] var1);

    boolean del(String... var1);

    Boolean exists(String var1);

    Long expire(String var1, int var2);

    Long append(byte[] var1, byte[] var2);

    Long decr(byte[] var1);

    Long decrBy(byte[] var1, long var2);

    byte[] getSet(byte[] var1, byte[] var2);

    Long incrBy(String var1, long var2);

    Long incrBy(byte[] var1, long var2);

    byte[] get(String var1);

    Long incr(String var1);

    boolean set(String var1, byte[] var2);

    boolean set(String var1, String var2, String var3, String var4, long var5);

    boolean setex(String var1, int var2, byte[] var3);

    List<byte[]> mget(List<String> var1);

    List<byte[]> mget(String... var1);

    boolean mset(Map<String, byte[]> var1);

    boolean setnx(String var1, byte[] var2);

    default <T> List<T> hgetAll(String key, Function<byte[], T> function) {
        Map<byte[], byte[]> map = this.hgetAll(SafeEncoder.encode(key));
        if (map != null && !map.isEmpty()) {
            Objects.requireNonNull(function);
            return (List)map.values().stream().map(function).collect(Collectors.toList());
        } else {
            return new ArrayList();
        }
    }

    default <T> T hget(String key, long field, Function<byte[], T> function) {
        Objects.requireNonNull(function);
        byte[] bytes = this.hget(key, long2bytes(field));
        return bytes == null ? null : function.apply(bytes);
    }

    default boolean hset(String key, long field, byte[] value) {
        return this.hset(SafeEncoder.encode(key), long2bytes(field), value);
    }

    default boolean hdel(String key, long field) {
        return this.hdel(SafeEncoder.encode(key), long2bytes(field));
    }

    default <T> List<T> hmget(String key, Function<byte[], T> function, long... fields) {
        if (fields != null && fields.length != 0) {
            Objects.requireNonNull(function);
            byte[][] byteArray = new byte[fields.length][];

            for(int i = 0; i < fields.length; ++i) {
                byteArray[i] = long2bytes(fields[i]);
            }

            return (List)this.hmget(SafeEncoder.encode(key), byteArray).stream().map(function).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    default <T> List<T> hmget(String key, Function<byte[], T> function, Collection<Long> fields) {
        if (fields != null && !fields.isEmpty()) {
            Objects.requireNonNull(function);
            byte[][] byteArray = new byte[fields.size()][];
            int i = 0;

            long l;
            for(Iterator var6 = fields.iterator(); var6.hasNext(); byteArray[i++] = long2bytes(l)) {
                l = (Long)var6.next();
            }

            return (List)this.hmget(SafeEncoder.encode(key), byteArray).stream().map(function).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    default Long hlen(String key) {
        return this.hlen(SafeEncoder.encode(key));
    }

    boolean hdel(byte[] var1, byte[] var2);

    Map<byte[], byte[]> hgetAll(byte[] var1);

    Set<byte[]> hkeys(byte[] var1);

    Long hlen(byte[] var1);

    List<byte[]> hmget(byte[] var1, byte[]... var2);

    boolean hset(byte[] var1, byte[] var2, byte[] var3);

    boolean hdel(String var1, String var2);

    Boolean hexists(String var1, String var2);

    byte[] hget(String var1, String var2);

    Map<String, byte[]> hgetAll(String var1);

    Long hincr(String var1, String var2);

    Long hincrBy(String var1, String var2, long var3);

    boolean hmset(String var1, Map<String, byte[]> var2);

    boolean hset(String var1, String var2, byte[] var3);

    boolean hsetnx(String var1, String var2, byte[] var3);

    Collection<byte[]> hvals(String var1);

    Boolean hexists(String var1, byte[] var2);

    Long hincr(String var1, byte[] var2);

    boolean hsetnx(String var1, byte[] var2, byte[] var3);

    byte[] hget(String var1, byte[] var2);

    byte[] lindex(byte[] var1, int var2);

    Long linsert(byte[] var1, LIST_POSITION var2, byte[] var3, byte[] var4);

    Long llen(byte[] var1);

    byte[] lpop(byte[] var1);

    Long lrem(byte[] var1, int var2, byte[] var3);

    Long lpush(String var1, byte[] var2);

    Long lpushStr(String var1, String var2);

    List<byte[]> lrange(String var1, int var2, int var3);

    String lset(String var1, int var2, byte[] var3);

    String ltrim(String var1, int var2, int var3);

    byte[] rpop(String var1);

    String rpopStr(String var1);

    Long rpush(String var1, byte[] var2);

    byte[] substr(byte[] var1, int var2, int var3);

    Long sadd(String var1, byte[]... var2);

    Long scard(String var1);

    Set<byte[]> sdiff(String... var1);

    Long sdiffstore(String var1, String... var2);

    Set<byte[]> sinter(String... var1);

    Long sinterstore(String var1, String... var2);

    Boolean sismember(String var1, byte[] var2);

    Set<byte[]> smembers(String var1);

    byte[] spop(String var1);

    List<byte[]> srandmember(String var1, int var2);

    byte[] srandmember(String var1);

    Long srem(String var1, byte[]... var2);

    Double zincrby(byte[] var1, double var2, byte[] var4);

    Set<byte[]> zrangeByScore(byte[] var1, double var2, double var4, int var6, int var7);

    Long zrem(byte[] var1, byte[] var2);

    Long zremrangeByRank(byte[] var1, int var2, int var3);

    Long zremrangeByScore(byte[] var1, double var2, double var4);

    Double zscore(byte[] var1, byte[] var2);

    Long zadd(String var1, double var2, byte[] var4);

    Long zadd(String var1, double var2, int var4);

    Long zcard(String var1);

    Long zcount(String var1, double var2, double var4);

    Set<byte[]> zrange(String var1, int var2, int var3);

    Set<Tuple> zrangeWithScores(String var1, int var2, int var3);

    Set<byte[]> zrangeByScore(String var1, double var2, double var4);

    Set<Tuple> zrangeByScoreWithScores(String var1, double var2, double var4);

    Set<Tuple> zrangeByScoreWithScores(byte[] var1, double var2, double var4, int var6, int var7);

    Long zrank(String var1, byte[] var2);

    Long zremrangeByScore(String var1, double var2, double var4);

    Set<byte[]> zrevrange(String var1, int var2, int var3);

    Set<Tuple> zrevrangeWithScores(String var1, int var2, int var3);

    Long zrevrank(String var1, byte[] var2);

    /** @deprecated */
    @Deprecated
    boolean watch(String var1);

    /** @deprecated */
    @Deprecated
    Transaction multi(String var1);

    /** @deprecated */
    @Deprecated
    boolean unwatch(String var1);

    boolean compareAndSet(String var1, byte[] var2);

    void syncPipeline(PipelineCommand var1, String... var2);

    List<Object> syncPipelineResult(PipelineCommand var1, String... var2);

    Set<byte[]> srandomMembers(byte[] var1, int var2);

    Object eval(String var1, int var2, String... var3);

    Object eval(String var1, List<String> var2, List<String> var3);

    Object eval(String var1);

    Object evalsha(String var1);

    Object evalsha(String var1, List<String> var2, List<String> var3);

    Object evalsha(String var1, int var2, String... var3);

    Boolean scriptExists(String var1);

    List<Boolean> scriptExists(String... var1);

    String scriptLoad(String var1);

    default JRedisLock tryLock(String key) {
        return JRedisLock.tryLock(this, key);
    }

    default JRedisLock tryLock(String key, String val) {
        return JRedisLock.tryLock(this, key, val);
    }

    static byte[] long2bytes(long input) {
        byte[] result = new byte[8];

        for(int i = 7; i >= 0; --i) {
            result[i] = (byte)((int)(input & 255L));
            input >>= 8;
        }

        return result;
    }

    static long bytes2long(byte[] bytes) {
        long result = 0L;
        result |= (long)(bytes[0] & 255);

        for(int i = 0; i < 8; ++i) {
            result <<= 8;
            result |= (long)(bytes[i] & 255);
        }

        return result;
    }
}

