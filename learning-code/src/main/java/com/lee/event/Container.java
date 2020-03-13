package com.lee.event;

import com.lee.util.log.Logging;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Container implements Contain {

//    Multimap<Integer, Subscriber> subscriberMap = ArrayListMultimap.create();

    Map<Integer, List<Subscriber>> subscriberMap = new HashMap<>();

    public void post(Object object) {
        List<Subscriber> subscribers = subscriberMap.get(getEventType(object));

        if (subscribers != null && !subscribers.isEmpty()) {

            for (Subscriber subscriber : subscribers) {
                try {
                    subscriber.onEvent(object);
                } catch (Throwable t) {
                    Logging.error("Event subscriber throws {}", t);
                }
            }
        }
    }

    private int getEventType(Object object) {
        return object.getClass().getAnnotation(Event.class).value();
    }

    @Override
    public void regist(Subscriber subscriber) {
        getOrNew(subscriber.type).add(subscriber);
    }

    private List<Subscriber> getOrNew(int type) {
        List<Subscriber> subscribers = subscriberMap.get(type);
        return subscribers == null ? getSafely(type) : subscribers;
    }

    private synchronized List<Subscriber> getSafely(int type) {
        List<Subscriber> subscribers = subscriberMap.get(type);
        if (subscribers == null) {
            subscribers = new CopyOnWriteArrayList<>();
            subscriberMap.put(type, subscribers);
        }
        return subscribers;
    }

    @Override
    public void unregist(int group) {
        for (List<Subscriber> list : subscriberMap.values()) {

            if (list != null) {
                Iterator<Subscriber> it = list.iterator();
                while (it.hasNext()) {
                    Subscriber subscriber = it.next();
                    if (subscriber.group == group) {
                        list.remove(subscriber);
                    }
                }
            }

        }
    }

    @Override
    public void unregist(Subscriber subscriber) {
        List<Subscriber> list = subscriberMap.get(subscriber.type);
        if (list != null && !list.isEmpty()) {
            list.remove(subscriber);
        }
    }
}
