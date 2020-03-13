package com.lee.event.entity;

import com.lee.event.Event;
import com.lee.event.EventType;

@Event(EventType.TEST_GET_EVENT_TYPE)
public class TestEventEntity {

    public final String content;

    public TestEventEntity(String content) {
        this.content = content;
    }

}
