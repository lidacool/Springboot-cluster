package com.lee.timer.one.repo;

public interface TimerRepo {

    boolean lock(String key, long nowTime);

    void unlock(String key);
}
