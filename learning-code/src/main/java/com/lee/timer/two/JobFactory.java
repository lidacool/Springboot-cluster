package com.lee.timer.two;

import org.quartz.*;

public abstract class JobFactory implements Job {

    /**
     * 不允许重写
     * @param scheduled
     */
    public final void startJob(Scheduler scheduled) {
        try {
            startJob(scheduled, getCommonJob());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    protected abstract CommonJob getCommonJob();

    /**
     * 私有的方法子类是无法继承的，也不存在覆盖
     * 我们不可能完全继承父母的一切（如性格等），但是父母的一些无法继承的东西却仍会深刻的影响着我们。
     * Abstract和private也是不可以一起使用的
     * @param scheduler
     * @param commonJob
     * @throws SchedulerException
     */
    private void startJob(Scheduler scheduler, CommonJob commonJob) throws SchedulerException {
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
