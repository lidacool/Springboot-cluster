package com.lee.util.jredis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JRedisLock {
    private static final int EXPIRY_SECONDS = 1;
    private static final long EXPIRY_TIME;
    private static final JRedisLock FAILED;
    private final JRedis jRedis;
    private final String key;
    private final String val;
    private long lockTime;
    static final String UNLOCK_SCRIPT;
    static String UNLOCK_SCRIPT_SHA1;

    static String readResource(String name) {
        try {
            InputStream dolockInput = JRedisLock.class.getClassLoader().getResourceAsStream(name);
            Throwable var2 = null;

            Object var5;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(dolockInput));
                Throwable var4 = null;

                try {
                    var5 = String.join("\n", (Iterable)reader.lines().collect(Collectors.toList()));
                } catch (Throwable var30) {
                    var5 = var30;
                    var4 = var30;
                    throw var30;
                } finally {
                    if (reader != null) {
                        if (var4 != null) {
                            try {
                                reader.close();
                            } catch (Throwable var29) {
                                var4.addSuppressed(var29);
                            }
                        } else {
                            reader.close();
                        }
                    }

                }
            } catch (Throwable var32) {
                var2 = var32;
                throw var32;
            } finally {
                if (dolockInput != null) {
                    if (var2 != null) {
                        try {
                            dolockInput.close();
                        } catch (Throwable var28) {
                            var2.addSuppressed(var28);
                        }
                    } else {
                        dolockInput.close();
                    }
                }

            }

            return (String)var5;
        } catch (IOException var34) {
            return null;
        }
    }

    static synchronized void initial(JRedis jRedis) {
        if (UNLOCK_SCRIPT_SHA1 == null) {
            try {
                UNLOCK_SCRIPT_SHA1 = jRedis.scriptLoad(UNLOCK_SCRIPT);
            } catch (Exception var2) {
                UNLOCK_SCRIPT_SHA1 = "";
            }
        }

    }

    private JRedisLock(JRedis jRedis, String key, String val, long lockTime) {
        this.jRedis = jRedis;
        this.key = key;
        this.val = val;
        this.lockTime = lockTime;
    }

    public static JRedisLock tryLock(JRedis jRedis, String key) {
        long nanoTime = System.nanoTime();
        String val = String.valueOf(nanoTime);
        return setValNX(jRedis, key, val) ? new JRedisLock(jRedis, key, val, nanoTime) : FAILED;
    }

    public static JRedisLock tryLock(JRedis jRedis, String key, String val) {
        return setValNX(jRedis, key, val) ? new JRedisLock(jRedis, key, val, System.nanoTime()) : FAILED;
    }

    private static boolean setValNX(JRedis jRedis, String key, String val) {
        return jRedis.set(key, val, "NX", "EX", 1L);
    }

    public boolean isLocked() {
        return this.lockTime != -1L;
    }

    public void release() {
        if (this.lockTime != -1L && System.nanoTime() - this.lockTime < EXPIRY_TIME) {
            if (UNLOCK_SCRIPT_SHA1 == null) {
                initial(this.jRedis);
            }

            if (UNLOCK_SCRIPT_SHA1 != null && UNLOCK_SCRIPT_SHA1.length() != 0) {
                byte[] value = this.jRedis.get(this.key);
                if (value != null && (new String(value)).equals(this.val)) {
                    this.jRedis.del(new String[]{this.key});
                }
            } else {
                this.jRedis.evalsha(UNLOCK_SCRIPT_SHA1, 1, new String[]{this.key, this.val});
            }

            this.lockTime = -1L;
        }

    }

    static {
        EXPIRY_TIME = TimeUnit.SECONDS.toNanos(1L);
        FAILED = new JRedisLock((JRedis)null, (String)null, (String)null, -1L);
        UNLOCK_SCRIPT = readResource("unlock.script");
    }
}
