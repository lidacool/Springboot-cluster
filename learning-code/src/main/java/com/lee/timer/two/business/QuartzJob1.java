package com.lee.timer.two.business;

import com.lee.timer.two.CommonJob;
import com.lee.timer.two.JobFactory;
import com.lee.util.log.Logging;
import org.quartz.JobExecutionContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务业务处理类，可以继承QuartzJobBean
 * 重写executeInternal方法来实现具体的定时业务逻辑
 */
@Business
public class QuartzJob1 extends JobFactory {

    @Override
    public void execute(JobExecutionContext context) {

        System.out.println("开始：" + System.currentTimeMillis());
        //获取JobDetail中关联的数据
        String msg = (String) context.getJobDetail().getJobDataMap().get("msg");
        Logging.info("QuartzJob1 current time :{}{}{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "---", msg);
        System.out.println("结束：" + System.currentTimeMillis());
    }

    @Override
    public CommonJob getCommonJob() {
        return new CommonJob("job1", "group1", "msg", "this's job1", "0/10 * 10 * * ?", getClass());
    }
}
