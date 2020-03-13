package com.lee;

import com.lee.exception.BusinessException;
import com.lee.returnOp.Status;
import com.lee.util.log.Logging;
import com.lee.util.net.NetUtil;
import com.lee.util.time.TimeUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 环绕通知 ProceedingJoinPoint 执行proceed方法的作用是让目标方法执行，
 * 这也是环绕通知和前置、后置通知方法的一个最大区别。简单理解，环绕通知=前置+目标方法执行+后置通知，
 * proceed方法就是用于启动目标方法执行的
 * 可以用于简单的日志操作，请求拦截，返回控制
 */
@Aspect
@Component
public class ControllerInterceptor {


    /**
     * 1、execution(): 表达式主体。
     * <p>
     * 2、第一个*号：表示返回类型，*号表示所有的类型。
     * <p>
     * 3、包名：表示需要拦截的包名，后面的两个句点表示当前包和当前包的所有子包，com.sample.service.impl包、子孙包下所有类的方法。
     * <p>
     * 4、第二个*号：表示类名，*号表示所有的类。
     * <p>
     * 5、*(..):最后这个星号表示方法名，*号表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数。
     */
    @Pointcut("execution(* com.lee.controllerWeb..*.*(..))")//此处更名不再拦截admin里面的controller
    public void aspect() {
    }

    @Around("aspect()")
    public Object aspect(ProceedingJoinPoint invocation) throws Throwable {
        long start = TimeUtil.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = (Map<String, Object>) invocation.proceed();
            result.put("success", true);
            result.put("status", Status.success);
            result.put("data", data);
            return result;
        } catch (BusinessException e) {
            String msg = e.getMsg();
            int code = e.getCode();
            result.put("status", code);
            result.put("data", msg == null ? "系统异常" : msg);
            Logging.error("error code:" + code + "," + msg);
            return result;
        } catch (Exception e) {
            String msg = e.getMessage();
            result.put("status", Status.fail);
            result.put("data", msg == null ? "系统异常" : msg);
            e.printStackTrace();
            return result;
        } finally {
            long end = TimeUtil.currentTimeMillis();
            Logging.info("{},consume time: {}ms",
                    NetUtil.findClientIPStr(),
                    (end - start));
        }
    }

}
