package com.lee.util.jredis;

import com.lee.util.jredis.algo.ShardAlgorithm;
import com.lee.util.jredis.algo.ShardAlgorithmFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


public class JRedisFactory {

    public JRedisFactory() {
    }

    public static JRedis createJredis(String masterUrls, String slaveUrls) {
        return new JRedisProxy(createAlgo(masterUrls, slaveUrls));
    }

    private static ShardAlgorithm createAlgo(String masterUrls, String slaveUrls) {

        String[] masterArr = masterUrls.split(",");
        String[] slaveArr = slaveUrls.split(",");

        if (masterArr.length != slaveArr.length) {
            throw new IllegalArgumentException("master and slavr must have same number");
        } else {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setTimeBetweenEvictionRunsMillis(60000L);
            config.setMaxTotal(100);
            config.setMaxIdle(50);
            config.setBlockWhenExhausted(false);
            config.setTestWhileIdle(true);

            ShardRedisPool[] pools = new ShardRedisPool[masterArr.length];

            int index = 0;

            for (int i = 0; i < masterArr.length; i++) {
                String master = masterArr[i];
                String[] splitted = master.split(":");
                String slave = slaveArr[index];
                String[] slaveSplitted = slave.split(":");

                if (splitted.length < 3 || slaveSplitted.length < 2) {
                    throw new IllegalArgumentException(master + " is not a valid redis server address");
                }

                RedisShardInfo info = splitted.length >= 4 ? new RedisShardInfo(index, splitted[0], splitted[1], Integer.parseInt(splitted[2]), findPassword(splitted), slaveSplitted[0], Integer.parseInt(slaveSplitted[1])) : new RedisShardInfo(index, splitted[0], splitted[1], Integer.parseInt(splitted[2]), slaveSplitted[0], Integer.parseInt(slaveSplitted[1]));

                pools[index] = new ShardRedisPool(config, info);

            }
            return ShardAlgorithmFactory.newShardAlgorithm(pools);
        }

    }

    private static String findPassword(String[] splitted) {
        StringBuilder sb = new StringBuilder();
        if (splitted.length == 4) {
            sb.append(splitted[3]);
        } else {

            for (int i = 3; i < splitted.length; i++) {
                sb.append(splitted[i]);
                if (i != splitted.length - 1) {
                    sb.append(";");
                }
            }
        }
        return sb.toString();
    }

}
