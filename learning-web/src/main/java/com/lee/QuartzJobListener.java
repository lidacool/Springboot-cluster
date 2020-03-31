package com.lee;

import com.lee.timer.two.QuartzConfig;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class QuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private QuartzConfig quartzConfig;

    /**
     * 初始启动quartz
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            quartzConfig.startJob();
            System.out.println("任务已经启动...");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始注入scheduler  这种方式得自己调用该方法获取bean 运用autowired则无需该方法
     * @return
     * @throws SchedulerException
     */
//    @Bean
//    public Scheduler scheduler() throws SchedulerException{
//        SchedulerFactory schedulerFactoryBean = new StdSchedulerFactory();
//        return schedulerFactoryBean.getScheduler();
//    }

}
