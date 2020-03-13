package com.lee.timer;

import com.lee.InjectUtil;
import com.lee.util.log.Logging;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务的实现
 * ScheduledExecutorService.scheduleAtFixedRate
 * */
public class TimerTask {

    private static ScheduledExecutorService executor;
    private static TimerTask task;
    private final TimerTaskExecutor taskExecutor;

    private <T> TimerTask(T t) {
        this.taskExecutor = (TimerTaskExecutor) t;
    }

    public static void start(){
        if (executor != null && task != null) {
            return;
        }
        executor = Executors.newScheduledThreadPool(1);

        task = new TimerTask(InjectUtil.getBean("timerTaskExecutor"));
        task.start2();
    }

    private void start2() {
        try {
            Runnable runnable = new TaskThread(taskExecutor);
            executor.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.MINUTES);
        } catch (Throwable t) {
            Logging.error(t.getMessage());
        }
    }

    public static void destory() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
            task = null;
        }
    }

    public static boolean isDestory() {
        if (executor != null) {
            return executor.isShutdown();
        }
        return true;
    }

    public static boolean immediateSatrt() {
        try {
            new Thread(() -> task.taskExecutor.execute()).start();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
