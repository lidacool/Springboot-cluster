package com.lee.timer.two.business;

import com.lee.timer.two.CommonJob;
import com.lee.timer.two.JobFactory;
import com.lee.util.log.Logging;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Business(value = false)
public class QuartzJob2 extends JobFactory {

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("开始：" + System.currentTimeMillis());
        //获取JobDetail中关联的数据
        String msg = (String) context.getJobDetail().getJobDataMap().get("msg");
        Logging.info("QuartzJob2 current time :{}{}{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "---", msg);
        System.out.println("结束：" + System.currentTimeMillis());
    }

    @Override
    public CommonJob getCommonJob() {
        return new CommonJob("job2", "group2", "msg", "this's job2", "0/30 * 10 * * ?", getClass());
    }

}
