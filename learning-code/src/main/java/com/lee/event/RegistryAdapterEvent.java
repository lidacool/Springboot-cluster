package com.lee.event;

import com.lee.util.log.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


/**
 * 执行事件的地方必须实现此类 或者RegistryAdapter
 */
@Component
public abstract class RegistryAdapterEvent implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    @Autowired
    private Container container;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        Class clazz = this.getClass();
        Logging.info("event class:{} start be listening", clazz.getName());
        Registry.regist(this, container);
    }
}
