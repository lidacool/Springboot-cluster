package com.lee.timer;

import com.lee.timer.repo.TimerRepo;
import com.lee.util.log.Logging;
import com.lee.util.random.RandomUtil;
import com.lee.util.string.StringUtil;
import com.lee.util.time.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


/**
 * TaskExecutor 与 ThreadPoolTaskExecutor getBean 时的冲突
 * 此类名不能写成 TaskExecutor
 *
 * 在执行 ApplicationContext.getBean(TaskExecutor.class)时拿到的 bean 是 ThreadPoolTaskExecutor
 * 导致后面使用时会报错
 * */
@Component
public class TimerTaskExecutor {

    private static final String LOCK_KEY_BYREDIS = "lockKeyTimer:";

    @Autowired
    private TimerRepo timerRepo;

    public void execute() {
        boolean lock = false;
        long now = getHourToDay();

        try {
            lock = timerRepo.lock(LOCK_KEY_BYREDIS, now);
            Logging.info("is unlock=" + lock);
            //运用锁的方法实现定时
            if (!lock) {
                return;
            }
            executeTask(RandomUtil.randomInt(1,10));
        } catch (Exception e) {
             if (lock) {
                 //加锁成功 但是执行方法失败，没必要可以不解锁，慎重
//             即使出错了也别抹除掉已经lock的事实，
                 timerRepo.unlock(LOCK_KEY_BYREDIS);
             }
            Logging.error(e.getMessage(), e);
        }
    }

    public static long getHourToDay(){
        long now2 = TimeUtil.currentTimeMillis()+TimeUnit.HOURS.toMillis(8L);
        long toDay = TimeUnit.MILLISECONDS.toDays(now2);
        return TimeUnit.MILLISECONDS.toHours(now2-TimeUnit.DAYS.toMillis(toDay));
    }

    private void executeTask(int randomInt) throws InterruptedException {

        List<Thread> threads = new ArrayList<>(randomInt);
        IntStream.range(1,randomInt+1).forEach(i->{
            Thread thread = new TaskThreadEx(i);
            thread.start();
            threads.add(thread);
        });

        for (Thread thread:threads) {
            thread.join();
        }
    }

    private static class TaskThreadEx extends Thread{
        final int test;

        public TaskThreadEx(int test) {
            this.test = test;
        }

        @Override
        public void run() {
            System.out.println(StringUtil.format("模拟的当前第{}个用户操作",test));
        }
    }


}
