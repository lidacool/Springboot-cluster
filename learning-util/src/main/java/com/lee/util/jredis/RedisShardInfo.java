package com.lee.util.jredis;

import redis.clients.jedis.Jedis;

public class RedisShardInfo {

    private final int shardId;
    private final String shardName;
    private final int timeout;
    private final String masterHost;
    private final int masterPort;
    private final String masterPassword;
    private final String slaveHost;
    private final int slavePort;

    public RedisShardInfo(int shardId, String shardName, String masterHost, String slaveHost) {
        this(shardId, shardName, masterHost, 6379, slaveHost, 6379);
    }

    public RedisShardInfo(int shardId, String shardName, String masterHost, int masterPort, String slaveHost, int slavePort) {
        this(shardId, shardName, masterHost, masterPort, slaveHost, slavePort, 15000);
    }

    public RedisShardInfo(int shardId, String shardName, String masterHost, int masterPort, String slaveHost, int slavePort, int timeout) {
        this(shardId, shardName, masterHost, masterPort, "", slaveHost, slavePort, timeout);
    }

    public RedisShardInfo(int shardId, String shardName, String masterHost, int masterPort, String masterPassword, String slaveHost, int slavePort) {
        this(shardId, shardName, masterHost, masterPort, masterPassword, slaveHost, slavePort, 15000);
    }

    public RedisShardInfo(int shardId, String shardName, String masterHost, int masterPort, String masterPassword, String slaveHost, int slavePort, int timeout) {
        this.shardId = shardId;
        this.shardName = shardName;
        this.masterHost = masterHost;
        this.masterPort = masterPort;
        this.masterPassword = masterPassword;
        this.slaveHost = slaveHost;
        this.slavePort = slavePort;
        this.timeout = timeout;
    }

    public boolean hasMasterPassword() {
        return this.masterPassword.trim().length() > 0;
    }

    public Jedis[] createResources(){
        return new Jedis[]{new Jedis(this.masterHost, this.masterPort, this.timeout), new Jedis(this.slaveHost, this.slavePort, this.timeout)};
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<Redis master='");
        sb.append(this.shardName);
        sb.append(":");
        sb.append(this.masterHost);
        sb.append(":");
        sb.append(this.masterPort);
        if (this.hasMasterPassword()) {
            sb.append(":");
            sb.append(this.masterPassword);
        }

        sb.append("' slave='");
        sb.append(this.slaveHost);
        sb.append(":");
        sb.append(this.slavePort);
        sb.append("'/>");
        return sb.toString();
    }

    public int getShardId() {
        return shardId;
    }

    public String getShardName() {
        return shardName;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getMasterHost() {
        return masterHost;
    }

    public int getMasterPort() {
        return masterPort;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public String getSlaveHost() {
        return slaveHost;
    }

    public int getSlavePort() {
        return slavePort;
    }
}
