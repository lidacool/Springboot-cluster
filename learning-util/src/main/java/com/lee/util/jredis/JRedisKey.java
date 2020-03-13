package com.lee.util.jredis;

public class JRedisKey {
    private static final String separator = "_";
    private final String key;
    private final long userId;
    private final String subKey;
    private static final byte default_cage = 0;

    public static byte[] toByteArray(String key, long userId, String subKey) {
        return (new JRedisKey(key, userId, subKey)).toByteArray();
    }

    public static byte[] toByteArray(String key, long userId) {
        return (new JRedisKey(key, userId)).toByteArray();
    }

    public static JRedisKey instance(byte[] bytes) {
        return new JRedisKey(bytes);
    }

    public static JRedisKey newKey(String key) {
        return new JRedisKey(key);
    }

    public static JRedisKey newKey(String key, long userId) {
        return new JRedisKey(key, userId);
    }

    public static JRedisKey newKey(String key, long userId, String subKey) {
        return new JRedisKey(key, userId, subKey);
    }

    public static JRedisKey newKey(String key, long userId, int subKey) {
        return new JRedisKey(key, userId, String.valueOf(subKey));
    }

    private JRedisKey(byte[] bytes) {
        String s = new String(bytes);
        String[] strArray = s.split("_");
        this.key = strArray[0];
        this.userId = Long.parseLong(strArray[1]);
        this.subKey = strArray.length > 2 ? strArray[2] : "";
    }

    private JRedisKey(String key) {
        this(key, 0L, "");
    }

    private JRedisKey(String key, long userId) {
        this(key, userId, "");
    }

    private JRedisKey(String key, long userId, String subKey) {
        this.key = key;
        this.userId = userId;
        this.subKey = subKey;
    }

    public String toString() {
        boolean hasSubKey = this.subKey != null && this.subKey.trim().length() > 0;
        return this.key + "_" + this.userId + (!hasSubKey ? "" : "_" + this.subKey);
    }

    public byte[] toByteArray() {
        return this.toString().getBytes();
    }

    public long getUserId() {
        return this.userId;
    }

    public String getKey() {
        return this.key;
    }

    public String getSubKey() {
        return this.subKey;
    }
}
