package com.lee.controllerWeb;

import com.lee.timer.two.QuartzConfig;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

/**
 * @RestController 注解相当于@ResponseBody ＋ @Controller合在一起的作用
 *
 * 1) 如果只是使用@RestController注解Controller，则Controller中的方法无法返回jsp页面，
 * 或者html，配置的视图解析器 InternalResourceViewResolver不起作用，返回的内容就是Return 里的内容。
 * 2) 如果需要返回到指定页面，则需要用 @Controller配合视图解析器InternalResourceViewResolver才行。
 *     如果需要返回JSON，XML或自定义mediaType内容到页面，则需要在对应的方法上加上@ResponseBody注解。
 */
@RestController
@RequestMapping("/quartz")
public class QuartzManagerController {

    @Autowired
    private QuartzConfig quartzConfig;

    @RequestMapping("/start")
    public Map<String,Object> startQuartzJob() {
        try {
            quartzConfig.startJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return Collections.singletonMap("status","ok");
    }

    @RequestMapping("/info")
    public String getQuartzJob(String name, String group) {
        String info = null;
        try {
            info = quartzConfig.getJobInfo(name, group);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return info;
    }

    @RequestMapping("/modify")
    public Map<String,Object> modifyQuartzJob(String name, String group, String time) {
        boolean flag = true;
        try {
            flag = quartzConfig.modifyJob(name, group, time);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Collections.singletonMap("status",flag);
    }

    @RequestMapping(value = "/pause")
    public Map<String,Object> pauseQuartzJob(String name, String group) {
        try {
            quartzConfig.pauseJob(name, group);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Collections.singletonMap("status","ok");
    }

    @RequestMapping(value = "/pauseAll")
    public Map<String,Object> pauseAllQuartzJob() {
        try {
            quartzConfig.pauseAllJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return Collections.singletonMap("status","ok");
    }

    @RequestMapping(value = "/resumeAllJob")
    public Map<String,Object> resumeAllJob() {
        try {
            quartzConfig.resumeAllJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return Collections.singletonMap("status","ok");
    }

    @RequestMapping(value = "/resumeJob")
    public Map<String,Object> resumeJob(String name, String group) {
        try {
            quartzConfig.resumeJob(name, group);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return Collections.singletonMap("status","ok");
    }

    @RequestMapping(value = "/delete")
    public Object deleteJob(String name, String group) {
        try {
            quartzConfig.deleteJob(name, group);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return "ok";
    }

}
