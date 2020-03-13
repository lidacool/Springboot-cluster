package com.lee.util.jredis;

import com.lee.util.jredis.algo.ShardAlgorithm;
import com.lee.util.jredis.pipeline.PipelineCommand;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;
import redis.clients.util.SafeEncoder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

public class JRedisProxy implements JRedis {
    private ShardAlgorithm shardingAlgo;
    private static PrintStream ps;
    private static boolean record = false;

    public JRedisProxy(ShardAlgorithm algo) {
        this.shardingAlgo = algo;
        initOperationTimeOutFile();
    }

    private static void initOperationTimeOutFile() {
        try {
            if (record && ps == null) {
                ps = new PrintStream(new FileOutputStream("/usr/local/services/jredisTimedOutOperation.dat"));
            }
        } catch (FileNotFoundException var1) {
            var1.printStackTrace();
        }

    }

    public String currentPoolUsage() {
        ShardRedisPool[] allPools = this.getAllPools();
        StringBuilder sb = new StringBuilder();
        ShardRedisPool[] var3 = allPools;
        int var4 = allPools.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            ShardRedisPool pool = var3[var5];
            String shardName = pool.getShardName();
            int numActive = pool.getNumActive();
            int numIdle = pool.getNumIdle();
            sb.append(shardName).append("{active=").append(numActive).append(",idle=").append(numIdle).append("}\r\n");
        }

        return sb.toString();
    }

    private ShardRedisPool[] getAllPools() {
        return this.shardingAlgo.getShardPools();
    }

    private ShardRedisPool getPool(byte[] key) {
        return this.shardingAlgo.shard(key);
    }

    private ShardRedisPool getPool(int shardId) {
        return this.shardingAlgo.shard(shardId);
    }

    private int getShardId(byte[] key) {
        return this.shardingAlgo.shardId(key);
    }

    public void disconnect() throws IOException {
        ShardRedisPool[] var1 = this.getAllPools();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ShardRedisPool pool = var1[var3];
            pool.destroy();
        }

    }

    private boolean convertStateReply(String reply) {
        return "OK".equals(reply);
    }

    private boolean convertStateReply(Number reply) {
        return 0 != reply.intValue();
    }

    public static void record() {
        record = true;
        initOperationTimeOutFile();
    }

    public static void disRecord() {
        record = false;
    }

    private void print(String op, String shardName) {
        if (ps != null && record) {
            ps.println(op + "  " + shardName + "  " + new Date());
        }

    }

    public boolean del(String... keys) {
        return this.del(stringArray2bytesArray(keys));
    }

    public static byte[][] stringArray2bytesArray(String... stringArray) {
        byte[][] bytesArray = new byte[stringArray.length][];

        for(int i = 0; i < stringArray.length; ++i) {
            bytesArray[i] = stringArray[i].getBytes();
        }

        return bytesArray;
    }

    public boolean del(byte[]... keys) {
        if (keys != null && keys.length != 0) {
            ShardRedisPool pool = this.getPool(keys[0]);
            Jedis jedis = pool.getResource();
            boolean broken = false;

            boolean var5;
            try {
                var5 = this.convertStateReply((Number)jedis.del(keys));
            } catch (Exception var9) {
                broken = true;
                this.print("del", pool.getShardName());
                throw new RuntimeException(var9);
            } finally {
                if (broken) {
                    pool.returnBrokenResource(jedis);
                } else {
                    pool.returnResource(jedis);
                }

            }

            return var5;
        } else {
            throw new IllegalArgumentException("null input when jRedisProxy del keys");
        }
    }

    public Long append(byte[] key, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var6;
        try {
            var6 = jedis.append(key, value);
        } catch (Exception var10) {
            broken = true;
            this.print("append", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Long decr(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var5;
        try {
            var5 = jedis.decr(key);
        } catch (Exception var9) {
            broken = true;
            this.print("decr", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Long decrBy(byte[] key, long integer) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var7;
        try {
            var7 = jedis.decrBy(key, integer);
        } catch (Exception var11) {
            broken = true;
            this.print("decrBy", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Boolean exists(String key) {
        return this.exists(SafeEncoder.encode(key));
    }

    public Boolean exists(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Boolean var5;
        try {
            var5 = jedis.exists(key);
        } catch (Exception var9) {
            broken = true;
            this.print("exists", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Long expire(String key, int seconds) {
        return this.expire(SafeEncoder.encode(key), seconds);
    }

    public Long expire(byte[] key, int seconds) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var6;
        try {
            var6 = jedis.expire(key, seconds);
        } catch (Exception var10) {
            broken = true;
            this.print("expire", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Long expireAt(byte[] key, long unixTime) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var7;
        try {
            var7 = jedis.expireAt(key, unixTime);
        } catch (Exception var11) {
            broken = true;
            this.print("expireAt", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public byte[] get(String key) {
        return this.get(SafeEncoder.encode(key));
    }

    public byte[] get(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        byte[] var5;
        try {
            var5 = jedis.get(key);
        } catch (Exception var9) {
            broken = true;
            this.print("get", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public boolean mset(Map<String, byte[]> keyValueMap) {
        Map<byte[], byte[]> bytesKeyValueMap = new HashMap(keyValueMap.size());
        Iterator var3 = keyValueMap.entrySet().iterator();

        while(var3.hasNext()) {
            Entry<String, byte[]> entry = (Entry)var3.next();
            bytesKeyValueMap.put(SafeEncoder.encode((String)entry.getKey()), entry.getValue());
        }

        return this.msetBytes(bytesKeyValueMap);
    }

    public boolean msetBytes(Map<byte[], byte[]> keyValueMap) {
        if (keyValueMap.isEmpty()) {
            return true;
        } else {
            Map<Integer, Map<byte[], byte[]>> shardKeyValueMap = this.shardKeyValueMap(keyValueMap);
            boolean result = false;

            int shard;
            Map shardKeyValue;
            for(Iterator var4 = shardKeyValueMap.entrySet().iterator(); var4.hasNext(); result |= this.msetByShard(shard, shardKeyValue)) {
                Entry<Integer, Map<byte[], byte[]>> entry = (Entry)var4.next();
                shard = (Integer)entry.getKey();
                shardKeyValue = (Map)entry.getValue();
            }

            return result;
        }
    }

    private Map<Integer, Map<byte[], byte[]>> shardKeyValueMap(Map<byte[], byte[]> keysValues) {
        Map<Integer, Map<byte[], byte[]>> shardKeyValueMap = new HashMap();
        Iterator var3 = keysValues.entrySet().iterator();

        while(var3.hasNext()) {
            Entry<byte[], byte[]> keyValue = (Entry)var3.next();
            byte[] key = (byte[])keyValue.getKey();
            int shardId = this.getShardId(key);
            Map<byte[], byte[]> shardKeyValue = (Map)shardKeyValueMap.get(shardId);
            if (shardKeyValue == null) {
                Map<byte[], byte[]> shardKeyValue2 = new HashMap();
                shardKeyValue2.put(key, keyValue.getValue());
                shardKeyValueMap.put(shardId, shardKeyValue2);
            } else {
                if (shardKeyValue.containsKey(key)) {
                    throw new IllegalArgumentException("repeated key!!!");
                }

                shardKeyValue.put(key, keyValue.getValue());
            }
        }

        return shardKeyValueMap;
    }

    private boolean msetByShard(int shard, Map<byte[], byte[]> keyValueMap) {
        List<byte[]> keyValueList = new ArrayList(keyValueMap.size() * 2);
        Iterator var4 = keyValueMap.entrySet().iterator();

        while(var4.hasNext()) {
            Entry<byte[], byte[]> keyValue = (Entry)var4.next();
            keyValueList.add(keyValue.getKey());
            keyValueList.add(keyValue.getValue());
        }

        byte[][] keysValues = (byte[][])keyValueList.toArray(new byte[keyValueList.size()][]);
        ShardRedisPool pool = this.getPool(shard);
        Jedis jedis = pool.getResource();

        boolean var7;
        try {
            var7 = this.convertStateReply(jedis.mset(keysValues));
        } catch (Exception var11) {
            pool.returnBrokenResource(jedis);
            throw new RuntimeException(var11);
        } finally {
            pool.returnResource(jedis);
        }

        return var7;
    }

    public List<byte[]> mget(List<String> keys) {
        return this.mget((String[])keys.toArray(new String[keys.size()]));
    }

    public List<byte[]> mget(String... keys) {
        return this.mget(stringArray2bytesArray(keys));
    }

    public List<byte[]> mget(byte[]... keys) {
        if (keys.length == 0) {
            return Collections.emptyList();
        } else {
            Map<Integer, List<byte[]>> shardKeysMap = this.shardKeysMap(keys);
            List<byte[]> result = new ArrayList(keys.length);
            Iterator var4 = shardKeysMap.entrySet().iterator();

            while(var4.hasNext()) {
                Entry<Integer, List<byte[]>> entry = (Entry)var4.next();
                int shard = (Integer)entry.getKey();
                List<byte[]> shardKeys = (List)entry.getValue();
                List<byte[]> mgetByShardResults = this.mgetByShard(shard, shardKeys);
                Iterator var9 = mgetByShardResults.iterator();

                while(var9.hasNext()) {
                    byte[] mgetByShardResult = (byte[])var9.next();
                    if (mgetByShardResult != null) {
                        result.add(mgetByShardResult);
                    }
                }
            }

            return result;
        }
    }

    private Map<Integer, List<byte[]>> shardKeysMap(byte[]... keys) {
        Map<Integer, List<byte[]>> shardKeysMap = new HashMap();
        byte[][] var3 = keys;
        int var4 = keys.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte[] key = var3[var5];
            int shardId = this.getShardId(key);
            List<byte[]> shardKeys = (List)shardKeysMap.get(shardId);
            if (shardKeys == null) {
                List<byte[]> shardKeys2 = new ArrayList();
                shardKeys2.add(key);
                shardKeysMap.put(shardId, shardKeys2);
            } else {
                shardKeys.add(key);
            }
        }

        return shardKeysMap;
    }

    private List<byte[]> mgetByShard(int shard, List<byte[]> keyList) {
        return this.mgetByShard(shard, (byte[][])keyList.toArray(new byte[keyList.size()][]));
    }

    private List<byte[]> mgetByShard(int shard, byte[]... keys) {
        ShardRedisPool pool = this.getPool(shard);
        Jedis jedis = pool.getResource();

        List var5;
        try {
            var5 = jedis.mget(keys);
        } catch (Exception var9) {
            pool.returnBrokenResource(jedis);
            throw new RuntimeException(var9);
        } finally {
            pool.returnResource(jedis);
        }

        return var5;
    }

    public byte[] getSet(byte[] key, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        byte[] var6;
        try {
            var6 = jedis.getSet(key, value);
        } catch (Exception var10) {
            broken = true;
            this.print("getSet", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public boolean hdel(String key, String field) {
        return this.hdel(SafeEncoder.encode(key), SafeEncoder.encode(field));
    }

    public boolean hdel(byte[] key, byte[] field) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        boolean var6;
        try {
            var6 = this.convertStateReply((Number)jedis.hdel(key, new byte[][]{field}));
        } catch (Exception var10) {
            broken = true;
            this.print("hdel", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Boolean hexists(String key, byte[] field) {
        return this.hexists(SafeEncoder.encode(key), field);
    }

    public Boolean hexists(String key, String field) {
        return this.hexists(SafeEncoder.encode(key), SafeEncoder.encode(field));
    }

    public Boolean hexists(byte[] key, byte[] field) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Boolean var6;
        try {
            var6 = jedis.hexists(key, field);
        } catch (Exception var10) {
            broken = true;
            this.print("hexists", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public byte[] hget(String key, byte[] field) {
        return this.hget(SafeEncoder.encode(key), field);
    }

    public byte[] hget(String key, String field) {
        return this.hget(SafeEncoder.encode(key), SafeEncoder.encode(field));
    }

    public byte[] hget(byte[] key, byte[] field) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        byte[] var6;
        try {
            var6 = jedis.hget(key, field);
        } catch (Exception var10) {
            broken = true;
            this.print("hget", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Map<String, byte[]> hgetAll(String key) {
        Map<byte[], byte[]> resultMap = this.hgetAll(SafeEncoder.encode(key));
        Map<String, byte[]> result = new HashMap(resultMap.size());
        Iterator var4 = resultMap.entrySet().iterator();

        while(var4.hasNext()) {
            Entry<byte[], byte[]> entry = (Entry)var4.next();
            result.put(SafeEncoder.encode((byte[])entry.getKey()), entry.getValue());
        }

        return result;
    }

    public Map<byte[], byte[]> hgetAll(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Map var5;
        try {
            var5 = jedis.hgetAll(key);
        } catch (Exception var9) {
            broken = true;
            this.print("hgetAll", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Long hincr(String key, byte[] field) {
        return this.hincrBy(SafeEncoder.encode(key), field, 1L);
    }

    public Long hincr(String key, String field) {
        return this.hincrBy(key, field, 1L);
    }

    public Long hincrBy(String key, String field, long value) {
        return this.hincrBy(SafeEncoder.encode(key), SafeEncoder.encode(field), value);
    }

    public Long hincrBy(byte[] key, byte[] field, long value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var8;
        try {
            var8 = jedis.hincrBy(key, field, value);
        } catch (Exception var12) {
            broken = true;
            this.print("hincrBy", pool.getShardName());
            throw new RuntimeException(var12);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var8;
    }

    public Set<byte[]> hkeys(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var5;
        try {
            var5 = jedis.hkeys(key);
        } catch (Exception var9) {
            broken = true;
            this.print("hkeys", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Long hlen(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var5;
        try {
            var5 = jedis.hlen(key);
        } catch (Exception var9) {
            broken = true;
            this.print("hlen", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        List var7;
        try {
            List<byte[]> bytesList = jedis.hmget(key, fields);
            var7 = this.filterNull(bytesList);
        } catch (Exception var11) {
            broken = true;
            this.print("hmget", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    private List<byte[]> filterNull(List<byte[]> bytesList) {
        List<byte[]> results = new ArrayList(bytesList.size());
        Iterator var3 = bytesList.iterator();

        while(var3.hasNext()) {
            byte[] bytes = (byte[])var3.next();
            if (bytes != null) {
                results.add(bytes);
            }
        }

        return results;
    }

    public boolean hmset(String key, Map<String, byte[]> fieldValueMap) {
        Map<byte[], byte[]> hash = new HashMap(fieldValueMap.size());
        Iterator var4 = fieldValueMap.entrySet().iterator();

        while(var4.hasNext()) {
            Entry<String, byte[]> entry = (Entry)var4.next();
            hash.put(SafeEncoder.encode((String)entry.getKey()), entry.getValue());
        }

        return this.hmset((byte[])SafeEncoder.encode(key), hash);
    }

    public boolean hmset(byte[] key, Map<byte[], byte[]> hash) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        boolean var6;
        try {
            var6 = this.convertStateReply(jedis.hmset(key, hash));
        } catch (Exception var10) {
            broken = true;
            this.print("hmset", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public boolean hset(String key, String field, byte[] value) {
        return this.hset(SafeEncoder.encode(key), SafeEncoder.encode(field), value);
    }

    public boolean hset(byte[] key, byte[] field, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        boolean var7;
        try {
            jedis.hset(key, field, value);
            var7 = true;
        } catch (Exception var11) {
            broken = true;
            this.print("hset", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public boolean hsetnx(String key, byte[] field, byte[] value) {
        return this.convertStateReply((Number)this.hsetnx(SafeEncoder.encode(key), field, value));
    }

    public boolean hsetnx(String key, String field, byte[] value) {
        return this.convertStateReply((Number)this.hsetnx(SafeEncoder.encode(key), SafeEncoder.encode(field), value));
    }

    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var7;
        try {
            var7 = jedis.hsetnx(key, field, value);
        } catch (Exception var11) {
            broken = true;
            this.print("hsetnx", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Collection<byte[]> hvals(String key) {
        return this.hvals(SafeEncoder.encode(key));
    }

    public Collection<byte[]> hvals(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        List var5;
        try {
            var5 = jedis.hvals(key);
        } catch (Exception var9) {
            broken = true;
            this.print("hvals", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Long incr(String key) {
        return this.incr(SafeEncoder.encode(key));
    }

    public Long incr(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var5;
        try {
            var5 = jedis.incr(key);
        } catch (Exception var9) {
            broken = true;
            this.print("incr", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Long incrBy(String key, long integer) {
        return this.incrBy(SafeEncoder.encode(key), integer);
    }

    public Long incrBy(byte[] key, long integer) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var7;
        try {
            var7 = jedis.incrBy(key, integer);
        } catch (Exception var11) {
            broken = true;
            this.print("incrBy", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public byte[] lindex(byte[] key, int index) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        byte[] var6;
        try {
            var6 = jedis.lindex(key, (long)index);
        } catch (Exception var10) {
            broken = true;
            this.print("lindex", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var8;
        try {
            var8 = jedis.linsert(key, where, pivot, value);
        } catch (Exception var12) {
            broken = true;
            this.print("linsert", pool.getShardName());
            throw new RuntimeException(var12);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var8;
    }

    public Long llen(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var5;
        try {
            var5 = jedis.llen(key);
        } catch (Exception var9) {
            broken = true;
            this.print("llen", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public byte[] lpop(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        byte[] var5;
        try {
            var5 = jedis.lpop(key);
        } catch (Exception var9) {
            broken = true;
            this.print("lpop", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Long lpushStr(String key, String string) {
        return this.lpush(SafeEncoder.encode(key), SafeEncoder.encode(string));
    }

    public Long lpush(String key, byte[] string) {
        return this.lpush(SafeEncoder.encode(key), string);
    }

    public Long lpush(byte[] key, byte[] string) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var6;
        try {
            var6 = jedis.lpush(key, new byte[][]{string});
        } catch (Exception var10) {
            broken = true;
            this.print("lpush", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Set<String> keys(String pattern) {
        ShardRedisPool pool = this.getAllPools()[0];
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var5;
        try {
            var5 = jedis.keys(pattern);
        } catch (Exception var9) {
            broken = true;
            this.print("keys", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public List<byte[]> lrange(String key, int start, int end) {
        return this.lrange(SafeEncoder.encode(key), start, end);
    }

    public List<byte[]> lrange(byte[] key, int start, int end) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        List var7;
        try {
            var7 = jedis.lrange(key, (long)start, (long)end);
        } catch (Exception var11) {
            broken = true;
            this.print("lrange", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Long lrem(byte[] key, int count, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var7;
        try {
            var7 = jedis.lrem(key, (long)count, value);
        } catch (Exception var11) {
            broken = true;
            this.print("lrem", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public String lset(String key, int index, byte[] value) {
        return this.lset(SafeEncoder.encode(key), index, value);
    }

    public String lset(byte[] key, int index, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        String var7;
        try {
            var7 = jedis.lset(key, (long)index, value);
        } catch (Exception var11) {
            broken = true;
            this.print("lset", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public String ltrim(String key, int start, int end) {
        return this.ltrim(SafeEncoder.encode(key), start, end);
    }

    public String ltrim(byte[] key, int start, int end) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        String var7;
        try {
            var7 = jedis.ltrim(key, (long)start, (long)end);
        } catch (Exception var11) {
            broken = true;
            this.print("ltrim", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public String rpopStr(String key) {
        return SafeEncoder.encode(this.rpop(key));
    }

    public byte[] rpop(String key) {
        return this.rpop(SafeEncoder.encode(key));
    }

    public byte[] rpop(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        byte[] var5;
        try {
            var5 = jedis.rpop(key);
        } catch (Exception var9) {
            broken = true;
            this.print("rpop", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Long rpush(String key, byte[] string) {
        return this.rpush(SafeEncoder.encode(key), string);
    }

    public Long rpush(byte[] key, byte[] string) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var6;
        try {
            var6 = jedis.rpush(key, new byte[][]{string});
        } catch (Exception var10) {
            broken = true;
            this.print("rpush", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Long sadd(String key, byte[]... member) {
        return this.sadd(SafeEncoder.encode(key), member);
    }

    public Long sadd(byte[] key, byte[]... member) {
        if (member != null && member.length != 0) {
            ShardRedisPool pool = this.getPool(key);
            Jedis jedis = pool.getResource();
            boolean broken = false;

            Long var6;
            try {
                var6 = jedis.sadd(key, member);
            } catch (Exception var10) {
                broken = true;
                this.print("sadd", pool.getShardName());
                throw new RuntimeException(var10);
            } finally {
                if (broken) {
                    pool.returnBrokenResource(jedis);
                } else {
                    pool.returnResource(jedis);
                }

            }

            return var6;
        } else {
            return 0L;
        }
    }

    public Long scard(String key) {
        return this.scard(SafeEncoder.encode(key));
    }

    public Long scard(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var5;
        try {
            var5 = jedis.scard(key);
        } catch (Exception var9) {
            broken = true;
            this.print("scard", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public boolean compareAndSet(String key, byte[] value) {
        return this.compareAndSet(SafeEncoder.encode(key), value);
    }

    public boolean compareAndSet(byte[] key, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        boolean var8;
        try {
            jedis.watch(new byte[][]{key});
            Transaction transaction = jedis.multi();
            transaction.set(key, value);
            List<Object> resultList = transaction.exec();
            var8 = resultList != null && !resultList.isEmpty();
        } catch (Exception var12) {
            broken = true;
            this.print("compareAndSet", pool.getShardName());
            throw new RuntimeException(var12);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var8;
    }

    public boolean set(String key, byte[] value) {
        return this.set(SafeEncoder.encode(key), value);
    }

    public boolean set(String key, String value, String nxxx, String expx, long time) {
        ShardRedisPool pool = this.getPool(SafeEncoder.encode(key));
        Jedis jedis = pool.getResource();
        boolean broken = false;

        boolean var10;
        try {
            var10 = this.convertStateReply(jedis.set(key, value, nxxx, expx, time));
        } catch (Exception var14) {
            broken = true;
            this.print("set", pool.getShardName());
            throw new RuntimeException(var14);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var10;
    }

    public boolean set(byte[] key, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        boolean var6;
        try {
            var6 = this.convertStateReply(jedis.set(key, value));
        } catch (Exception var10) {
            broken = true;
            this.print("set", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public boolean setex(String key, int seconds, byte[] value) {
        return this.setex(SafeEncoder.encode(key), seconds, value);
    }

    public boolean setex(byte[] key, int seconds, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        boolean var7;
        try {
            var7 = this.convertStateReply(jedis.setex(key, seconds, value));
        } catch (Exception var11) {
            broken = true;
            this.print("setex", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public boolean setnx(String key, byte[] value) {
        return this.setnx(SafeEncoder.encode(key), value);
    }

    public boolean setnx(byte[] key, byte[] value) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        boolean var6;
        try {
            var6 = jedis.setnx(key, value).intValue() == 1;
        } catch (Exception var10) {
            broken = true;
            this.print("setnx", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Boolean sismember(String key, byte[] member) {
        return this.sismember(SafeEncoder.encode(key), member);
    }

    public Boolean sismember(byte[] key, byte[] member) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Boolean var6;
        try {
            var6 = jedis.sismember(key, member);
        } catch (Exception var10) {
            broken = true;
            this.print("sismember", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Set<byte[]> smembers(String key) {
        return this.smembers(SafeEncoder.encode(key));
    }

    public Set<byte[]> smembers(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var5;
        try {
            var5 = jedis.smembers(key);
        } catch (Exception var9) {
            broken = true;
            this.print("smembers", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public List<byte[]> sort(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        List var5;
        try {
            var5 = jedis.sort(key);
        } catch (Exception var9) {
            broken = true;
            this.print("sort", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        List var6;
        try {
            var6 = jedis.sort(key, sortingParameters);
        } catch (Exception var10) {
            broken = true;
            this.print("sort", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public byte[] spop(String key) {
        return this.spop(SafeEncoder.encode(key));
    }

    public byte[] spop(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        byte[] var5;
        try {
            var5 = jedis.spop(key);
        } catch (Exception var9) {
            broken = true;
            this.print("spop", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public byte[] srandmember(String key) {
        return this.srandmember(SafeEncoder.encode(key));
    }

    public byte[] srandmember(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        byte[] var5;
        try {
            var5 = jedis.srandmember(key);
        } catch (Exception var9) {
            broken = true;
            this.print("srandmember", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public List<byte[]> srandmember(String key, int count) {
        return this.srandmember(SafeEncoder.encode(key), count);
    }

    public List<byte[]> srandmember(byte[] key, int count) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        List var6;
        try {
            var6 = jedis.srandmember(key, count);
        } catch (Exception var10) {
            broken = true;
            this.print("srandmember", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Long srem(String key, byte[]... member) {
        return this.srem(SafeEncoder.encode(key), member);
    }

    public Long srem(byte[] key, byte[]... member) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var6;
        try {
            var6 = jedis.srem(key, member);
        } catch (Exception var10) {
            broken = true;
            this.print("srem", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public byte[] substr(byte[] key, int start, int end) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        byte[] var7;
        try {
            var7 = jedis.substr(key, start, end);
        } catch (Exception var11) {
            broken = true;
            this.print("substr", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Long ttl(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var5;
        try {
            var5 = jedis.ttl(key);
        } catch (Exception var9) {
            broken = true;
            this.print("ttl", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public String type(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        String var5;
        try {
            var5 = jedis.type(key);
        } catch (Exception var9) {
            broken = true;
            this.print("type", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Long zadd(String key, double score, byte[] member) {
        return this.zadd(SafeEncoder.encode(key), score, member);
    }

    public Long zadd(String key, double score, int member) {
        return this.zadd(key, score, SafeEncoder.encode(String.valueOf(member)));
    }

    public Long zadd(byte[] key, double score, byte[] member) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var8;
        try {
            var8 = jedis.zadd(key, score, member);
        } catch (Exception var12) {
            broken = true;
            this.print("zadd", pool.getShardName());
            throw new RuntimeException(var12);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var8;
    }

    public Long zcard(String key) {
        return this.zcard(SafeEncoder.encode(key));
    }

    public Long zcard(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var5;
        try {
            var5 = jedis.zcard(key);
        } catch (Exception var9) {
            broken = true;
            this.print("zcard", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Long zcount(String key, double min, double max) {
        return this.zcount(SafeEncoder.encode(key), min, max);
    }

    public Long zcount(byte[] key, double min, double max) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var9;
        try {
            var9 = jedis.zcount(key, min, max);
        } catch (Exception var13) {
            broken = true;
            this.print("zcount", pool.getShardName());
            throw new RuntimeException(var13);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var9;
    }

    public Double zincrby(byte[] key, double score, byte[] member) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Double var8;
        try {
            var8 = jedis.zincrby(key, score, member);
        } catch (Exception var12) {
            broken = true;
            this.print("zincrby", pool.getShardName());
            throw new RuntimeException(var12);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var8;
    }

    public Set<byte[]> zrange(String key, int start, int end) {
        return this.zrange(SafeEncoder.encode(key), start, end);
    }

    public Set<byte[]> zrange(byte[] key, int start, int end) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var7;
        try {
            var7 = jedis.zrange(key, (long)start, (long)end);
        } catch (Exception var11) {
            broken = true;
            this.print("zrange", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Set<byte[]> zrangeByScore(String key, double min, double max) {
        return this.zrangeByScore(SafeEncoder.encode(key), min, max);
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var9;
        try {
            var9 = jedis.zrangeByScore(key, min, max);
        } catch (Exception var13) {
            broken = true;
            this.print("zrangeByScore", pool.getShardName());
            throw new RuntimeException(var13);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var9;
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var11;
        try {
            var11 = jedis.zrangeByScore(key, min, max, offset, count);
        } catch (Exception var15) {
            broken = true;
            this.print("zrangeByScore", pool.getShardName());
            throw new RuntimeException(var15);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var11;
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return this.zrangeByScoreWithScores(SafeEncoder.encode(key), min, max);
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var9;
        try {
            var9 = jedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception var13) {
            broken = true;
            this.print("zrangeByScoreWithScores", pool.getShardName());
            throw new RuntimeException(var13);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var9;
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var11;
        try {
            var11 = jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception var15) {
            broken = true;
            this.print("zrangeByScoreWithScores", pool.getShardName());
            throw new RuntimeException(var15);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var11;
    }

    public Set<Tuple> zrangeWithScores(String key, int start, int end) {
        return this.zrangeWithScores(SafeEncoder.encode(key), start, end);
    }

    public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var7;
        try {
            var7 = jedis.zrangeWithScores(key, (long)start, (long)end);
        } catch (Exception var11) {
            broken = true;
            this.print("zrangeWithScores", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Long zrank(String key, byte[] member) {
        return this.zrank(SafeEncoder.encode(key), member);
    }

    public Long zrank(byte[] key, byte[] member) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var6;
        try {
            var6 = jedis.zrank(key, member);
        } catch (Exception var10) {
            broken = true;
            this.print("zrank", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Long zrem(byte[] key, byte[] member) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var6;
        try {
            var6 = jedis.zrem(key, new byte[][]{member});
        } catch (Exception var10) {
            broken = true;
            this.print("zrem", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Long zremrangeByRank(byte[] key, int start, int end) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var7;
        try {
            var7 = jedis.zremrangeByRank(key, (long)start, (long)end);
        } catch (Exception var11) {
            broken = true;
            this.print("zremrangeByRank", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Long zremrangeByScore(String key, double start, double end) {
        return this.zremrangeByScore(SafeEncoder.encode(key), start, end);
    }

    public Long zremrangeByScore(byte[] key, double start, double end) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var9;
        try {
            var9 = jedis.zremrangeByScore(key, start, end);
        } catch (Exception var13) {
            broken = true;
            this.print("zremrangeByScore", pool.getShardName());
            throw new RuntimeException(var13);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var9;
    }

    public Set<byte[]> zrevrange(String key, int start, int end) {
        return this.zrevrange(SafeEncoder.encode(key), start, end);
    }

    public Set<byte[]> zrevrange(byte[] key, int start, int end) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var7;
        try {
            var7 = jedis.zrevrange(key, (long)start, (long)end);
        } catch (Exception var11) {
            broken = true;
            this.print("zrevrange", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
        return this.zrevrangeWithScores(SafeEncoder.encode(key), start, end);
    }

    public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Set var7;
        try {
            var7 = jedis.zrevrangeWithScores(key, (long)start, (long)end);
        } catch (Exception var11) {
            broken = true;
            this.print("zrevrangeWithScores", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Long zrevrank(String key, byte[] member) {
        return this.zrevrank(SafeEncoder.encode(key), member);
    }

    public Long zrevrank(byte[] key, byte[] member) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Long var6;
        try {
            var6 = jedis.zrevrank(key, member);
        } catch (Exception var10) {
            broken = true;
            this.print("zrevrank", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Double zscore(byte[] key, byte[] member) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Double var6;
        try {
            var6 = jedis.zscore(key, member);
        } catch (Exception var10) {
            broken = true;
            this.print("zscore", pool.getShardName());
            throw new RuntimeException(var10);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Set<byte[]> srandomMembers(byte[] key, int count) {
        long len = this.scard(key);
        if (len == 0L) {
            return null;
        } else if ((long)count >= len) {
            return this.smembers(key);
        } else {
            return (double)count / (double)len > 0.7D ? this.randomNeedAllMembers(key, count) : this.randomMembers(key, count);
        }
    }

    private Set<byte[]> randomMembers(byte[] key, int count) {
        Set<byte[]> members = new HashSet(count * 4 / 3);
        int i = 0;

        while(i < count) {
            byte[] member = this.srandmember(key);
            if (!this.contains(members, member) && members.add(member)) {
                ++i;
            }
        }

        return members;
    }

    private boolean contains(Collection<byte[]> members, byte[] member) {
        Iterator var3 = members.iterator();

        byte[] m;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            m = (byte[])var3.next();
        } while(!Arrays.equals(m, member));

        return true;
    }

    private Set<byte[]> randomNeedAllMembers(byte[] key, int count) {
        Set<byte[]> members = this.smembers(key);
        Set<byte[]> res = new HashSet();
        Iterator var5 = members.iterator();

        do {
            if (!var5.hasNext()) {
                return res;
            }

            byte[] bs = (byte[])var5.next();
            res.add(bs);
        } while(res.size() < count);

        return res;
    }

    public Set<byte[]> sdiff(String... keys) {
        return this.sdiff(stringArray2bytesArray(keys));
    }

    public Set<byte[]> sdiff(byte[]... keys) {
        this.checkKeysInSameShard(keys);
        ShardRedisPool pool = this.getPool(keys[0]);
        Jedis jedis = pool.getResource();

        Set var4;
        try {
            var4 = jedis.sdiff(keys);
        } finally {
            pool.returnResource(jedis);
        }

        return var4;
    }

    public Long sdiffstore(String dstkey, String... keys) {
        return this.sdiffstore(SafeEncoder.encode(dstkey), stringArray2bytesArray(keys));
    }

    public Long sdiffstore(byte[] dstkey, byte[]... keys) {
        this.checkKeysInSameShard(keys);
        ShardRedisPool pool = this.getPool(keys[0]);
        Jedis jedis = pool.getResource();

        Long var5;
        try {
            var5 = jedis.sdiffstore(dstkey, keys);
        } finally {
            pool.returnResource(jedis);
        }

        return var5;
    }

    public Set<byte[]> sinter(String... keys) {
        return this.sinter(stringArray2bytesArray(keys));
    }

    public Set<byte[]> sinter(byte[]... keys) {
        this.checkKeysInSameShard(keys);
        ShardRedisPool pool = this.getPool(keys[0]);
        Jedis jedis = pool.getResource();

        Set var4;
        try {
            var4 = jedis.sinter(keys);
        } finally {
            pool.returnResource(jedis);
        }

        return var4;
    }

    public Long sinterstore(String dstkey, String... keys) {
        return this.sinterstore(SafeEncoder.encode(dstkey), stringArray2bytesArray(keys));
    }

    public Long sinterstore(byte[] dstkey, byte[]... keys) {
        this.checkKeysInSameShard(keys);
        ShardRedisPool pool = this.getPool(keys[0]);
        Jedis jedis = pool.getResource();

        Long var5;
        try {
            var5 = jedis.sinterstore(dstkey, keys);
        } finally {
            pool.returnResource(jedis);
        }

        return var5;
    }

    public boolean watch(String key) {
        return this.watch(SafeEncoder.encode(key));
    }

    public boolean watch(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        boolean var5;
        try {
            var5 = this.convertStateReply(jedis.watch(new byte[][]{key}));
        } catch (Exception var9) {
            broken = true;
            this.print("watch", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Transaction multi(String key) {
        return this.multi(SafeEncoder.encode(key));
    }

    public Transaction multi(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Transaction var5;
        try {
            var5 = jedis.multi();
        } catch (Exception var9) {
            broken = true;
            this.print("multi", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public boolean unwatch(String key) {
        return this.unwatch(SafeEncoder.encode(key));
    }

    public boolean unwatch(byte[] key) {
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        boolean var5;
        try {
            var5 = this.convertStateReply(jedis.unwatch());
        } catch (Exception var9) {
            broken = true;
            this.print("unwatch", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public void syncPipeline(PipelineCommand command, String... keys) {
        this.syncPipeline(command, stringArray2bytesArray(keys));
    }

    public void syncPipeline(PipelineCommand command, byte[]... keys) {
        this.checkKeysInSameShard(keys);
        byte[] key = keys[0];
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        try {
            Pipeline pipeline = jedis.pipelined();
            command.execute(pipeline);
            pipeline.sync();
        } catch (Exception var11) {
            broken = true;
            this.print("syncPipeline", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

    }

    public List<Object> syncPipelineResult(PipelineCommand command, String... keys) {
        return this.syncPipelineResult(command, stringArray2bytesArray(keys));
    }

    public List<Object> syncPipelineResult(PipelineCommand command, byte[]... keys) {
        this.checkKeysInSameShard(keys);
        byte[] key = keys[0];
        ShardRedisPool pool = this.getPool(key);
        Jedis jedis = pool.getResource();
        boolean broken = false;

        List var8;
        try {
            Pipeline pipeline = jedis.pipelined();
            command.execute(pipeline);
            var8 = pipeline.syncAndReturnAll();
        } catch (Exception var12) {
            broken = true;
            this.print("syncPipelineResult", pool.getShardName());
            throw new RuntimeException(var12);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var8;
    }

    private void checkKeysInSameShard(byte[]... keys) {
        this.checkNotEmpty(keys);
        if (keys.length != 1) {
            int firstKeyShard = this.getShardId(keys[0]);

            for(int i = 1; i < keys.length; ++i) {
                int keyShard = this.getShardId(keys[i]);
                if (firstKeyShard != keyShard) {
                    throw new IllegalArgumentException("Pipeline Keys not In Same Shard!");
                }
            }

        }
    }

    private void checkNotEmpty(byte[]... bytes) {
        if (bytes.length == 0) {
            throw new IllegalArgumentException("bytes array is empty!");
        }
    }

    public Object eval(String script, int keyCount, String... params) {
        ShardRedisPool pool = this.getPool(script.getBytes(StandardCharsets.UTF_8));
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Object var7;
        try {
            var7 = jedis.eval(script, keyCount, params);
        } catch (Exception var11) {
            broken = true;
            this.print("eval", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Object eval(String script, List<String> keys, List<String> args) {
        ShardRedisPool pool = this.getPool(script.getBytes(StandardCharsets.UTF_8));
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Object var7;
        try {
            var7 = jedis.eval(script, keys, args);
        } catch (Exception var11) {
            broken = true;
            this.print("eval", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Object eval(String script) {
        ShardRedisPool pool = this.getPool(script.getBytes(StandardCharsets.UTF_8));
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Object var5;
        try {
            var5 = jedis.eval(script);
        } catch (Exception var9) {
            broken = true;
            this.print("eval", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Object evalsha(String script) {
        ShardRedisPool pool = this.getPool(script.getBytes(StandardCharsets.UTF_8));
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Object var5;
        try {
            var5 = jedis.evalsha(script);
        } catch (Exception var9) {
            broken = true;
            this.print("eval", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        ShardRedisPool pool = this.getPool(sha1.getBytes(StandardCharsets.UTF_8));
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Object var7;
        try {
            var7 = jedis.evalsha(sha1, keys, args);
        } catch (Exception var11) {
            broken = true;
            this.print("eval", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Object evalsha(String sha1, int keyCount, String... params) {
        ShardRedisPool pool = this.getPool(sha1.getBytes(StandardCharsets.UTF_8));
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Object var7;
        try {
            var7 = jedis.evalsha(sha1, keyCount, params);
        } catch (Exception var11) {
            broken = true;
            this.print("eval", pool.getShardName());
            throw new RuntimeException(var11);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var7;
    }

    public Boolean scriptExists(String sha1) {
        ShardRedisPool pool = this.getPool(sha1.getBytes(StandardCharsets.UTF_8));
        Jedis jedis = pool.getResource();
        boolean broken = false;

        Boolean var5;
        try {
            var5 = jedis.scriptExists(sha1);
        } catch (Exception var9) {
            broken = true;
            this.print("eval", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public List<Boolean> scriptExists(String... sha1) {
        ShardRedisPool pool = this.getPool(sha1[0].getBytes(StandardCharsets.UTF_8));
        Jedis jedis = pool.getResource();
        boolean broken = false;

        List var5;
        try {
            var5 = jedis.scriptExists(sha1);
        } catch (Exception var9) {
            broken = true;
            this.print("eval", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }

    public String scriptLoad(String script) {
        ShardRedisPool pool = this.getPool(script.getBytes(StandardCharsets.UTF_8));
        Jedis jedis = pool.getResource();
        boolean broken = false;

        String var5;
        try {
            var5 = jedis.scriptLoad(script);
        } catch (Exception var9) {
            broken = true;
            this.print("eval", pool.getShardName());
            throw new RuntimeException(var9);
        } finally {
            if (broken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }

        }

        return var5;
    }
}
