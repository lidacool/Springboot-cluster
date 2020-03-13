package com.lee.timer.repo;

import com.lee.util.jredis.JRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("timerRepo")
public class TimerRepoImpl implements TimerRepo {

    @Autowired
    private JRedis jRedis;

    @Override
    public boolean lock(String key, long nowTime) {

        if (jRedis.exists(key)) {
            long lastHour = JRedis.bytes2long(jRedis.get(key));
            if (lastHour == nowTime) {
                return false;
            }
        }

        jRedis.set(key, JRedis.long2bytes(nowTime));
        return true;
    }

    @Override
    public void unlock(String key) {
        long lastHour = JRedis.bytes2long(jRedis.get(key));
        jRedis.set(key, JRedis.long2bytes(--lastHour));
    }


}
