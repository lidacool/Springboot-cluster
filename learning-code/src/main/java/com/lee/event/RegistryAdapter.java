package com.lee.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

public abstract class RegistryAdapter {
//
//    @Autowired
//    private Container container;
//
//
//    /**
//     * ApplicationFailedEvent：该事件为spring boot启动失败时的操作
//     *
//     * ApplicationPreparedEvent：上下文context准备时触发
//     *
//     * ApplicationReadyEvent：上下文已经准备完毕的时候触发
//     *
//     * ApplicationStartedEvent：spring boot 启动监听类
//     *
//     * SpringApplicationEvent：获取SpringApplication
//     *
//     * ApplicationEnvironmentPreparedEvent：环境事先准备
//     * 亦可以通过实现这些接口监听
//     * */
//    @EventListener(ApplicationReadyEvent.class)
//    public void initialAfterApplicationReady() {
//        Registry.regist(this, container);
//    }
}
