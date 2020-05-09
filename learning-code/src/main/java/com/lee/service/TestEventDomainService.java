package com.lee.service;

import com.lee.event.RegistryAdapterEvent;
import com.lee.event.Subscribe;
import com.lee.event.entity.TestEventEntity;
import com.lee.timer.three.Task;
import com.lee.timer.three.TaskContext;
import com.lee.util.log.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TestEventDomainService extends RegistryAdapterEvent {

    @Autowired
    private TaskContext taskContext;


    /**
     * 此方法如何被执行？
     * 1.此类继承自RegistryAdapter
     * 2. RegistryAdapter执行initialAfterApplicationReady会传入此类
     * 3.抛出事件的地方会走到onevent ，那里便根据@Subscribe获取此方法接受抛事件传入的类
     * */
    @Subscribe
    public void onEventTest(TestEventEntity eventEntity) {
        taskContext.schedule(Task.once("onEventTest", 10, TimeUnit.SECONDS, () -> {
            Logging.info("这里是异步事件调用，另起线程异步执行的方法，可以用来缓解单线程不需要实时同步的压力!");
            Logging.info(eventEntity.content);
        }));
    }

}
