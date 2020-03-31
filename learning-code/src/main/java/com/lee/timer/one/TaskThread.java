package com.lee.timer.one;

import com.lee.util.log.Logging;
import com.lee.util.time.TimeUtil;

import java.util.concurrent.TimeUnit;

public class TaskThread implements Runnable {

    private TimerTaskExecutor taskExecutor;

    public TaskThread(TimerTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void run() {
        Logging.info("execute " + getClass().getSimpleName() + " begin...");
        long begin = TimeUtil.currentTimeMillis();
        taskExecutor.execute();
        long end = TimeUtil.currentTimeMillis();
        Logging.info(
                "execute " + getClass().getSimpleName() + " spend:" + TimeUnit.MILLISECONDS.toSeconds(end - begin) + "s");
        Logging.info("execute " + getClass().getSimpleName() + " end...");
    }
}
