package com.lee.timer.repo;

public interface TimerRepo {

    boolean lock(String key, long nowTime);

    void unlock(String key);
}
