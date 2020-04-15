package com.lee.timer.two;

import org.quartz.Job;

public class CommonJob {
    private String job;
    private String group;
    private String msg;
    private String desc;
    private String coreTime;
    private Class<? extends Job> aClass;

    public CommonJob(String job, String group, String msg, String desc, String coreTime, Class<? extends Job> aClass) {
        this.job = job;
        this.group = group;
        this.msg = msg;
        this.desc = desc;
        this.coreTime = coreTime;
        this.aClass = aClass;
    }

    public String getJob() {
        return job;
    }

    public String getGroup() {
        return group;
    }

    public String getMsg() {
        return msg;
    }

    public String getDesc() {
        return desc;
    }

    public String getCoreTime() {
        return coreTime;
    }

    public Class<? extends Job> getaClass() {
        return aClass;
    }
}
