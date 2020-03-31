package com.lee.timer.two;

import com.lee.timer.two.business.Business;
import com.lee.util.classUtil.ClassUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Set;

/**
 * quartz 多任务动态管理
 */
@Configuration
public class QuartzConfig {

    /**
     * Pro 1 集群下重复执行问题
     *
     * no.1 Redis/Zookeeper 分布式锁--执行周期频繁的任务 加锁略显不适
     * no.2 配置开关--注解控制、配置常量、配置数据库  维护代价高
     * no.3 Elastic Job
     */

    // 任务调度
    @Autowired
    private Scheduler scheduler;

    /**
     * 开始执行所有任务
     *
     * @throws SchedulerException
     */
    public void startJob() throws SchedulerException {

        Set<Class<?>> classSet = ClassUtil.getAllClassByAnnotation("com.lee.timer.two.business", Business.class);

        try {
            for (Class<?> cls : classSet) {
                if (cls.getAnnotation(Business.class).value()) {
                    JobFactory instance = (JobFactory) cls.newInstance();
                    instance.startJob(scheduler);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        scheduler.start();
    }

    /**
     * 获取Job信息
     *
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 修改某个任务的执行时间
     *
     * @param name
     * @param group
     * @param time
     * @return
     * @throws SchedulerException
     */
    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .usingJobData("msg", "hollo i'm ".concat(name))
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

//单个任务执行方法
//    @Bean
//    public JobDetail printTimeJobDetail(){
//        return JobBuilder.newJob(QuartzJob1.class)//PrintTimeJob我们的业务类
//                .withIdentity("PrintTimeJob")//可以给该JobDetail起一个id
//                //每个JobDetail内都有一个Map，包含了关联到这个Job的数据，在Job类中可以通过JobExecutionContext获取
//                .usingJobData("msg", "Hello Quartz")//关联键值对
//                .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
//                .build();
//    }
//    @Bean
//    public Trigger printTimeJobTrigger() {
//        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/5 14 * * ?");
//        return TriggerBuilder.newTrigger()
//                .forJob(printTimeJobDetail())//关联上述的JobDetail
//                .withIdentity("quartzTaskService")//给Trigger起个名字
//                .withSchedule(cronScheduleBuilder)
//                .build();
//    }
}
