package com.lee.timer.two;

import org.quartz.*;

public abstract class JobFactory implements Job {

    public void startJob(Scheduler scheduled) {
        try {
            startJob(scheduled, getCommonJob());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    protected abstract CommonJob getCommonJob();

    /**
     * 私有的方法子类是无法继承的，也不存在覆盖
     * Abstract和private也是不可以一起使用的
     * @param scheduler
     * @param commonJob
     * @throws SchedulerException
     */
    private static void startJob(Scheduler scheduler, CommonJob commonJob) throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(commonJob.getaClass())
                .withIdentity(commonJob.getJob(), commonJob.getGroup())
                .usingJobData(commonJob.getMsg(), commonJob.getDesc())
                .build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(commonJob.getCoreTime());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(commonJob.getJob(), commonJob.getGroup())
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }


}
