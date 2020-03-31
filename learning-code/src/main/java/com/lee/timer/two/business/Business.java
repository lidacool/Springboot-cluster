package com.lee.timer.two.business;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Business {

    /**
     * 根据value值判断被该注解标记的业务类是否加入quartz多任务
     * 当某任务不在使用  但是不方便删除时 value置为false即可
     *
     * @return
     */
    boolean value() default true;
}
