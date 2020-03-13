package com.lee.event;

import java.util.function.Consumer;

public abstract class Subscriber {

    final int group;
    final int type;

    public Subscriber(int group, int type) {
        this.group = group;
        this.type = type;
    }

    public abstract void onEvent(Object object) throws Exception;

    public static final class SimpleSubscriber<X> extends Subscriber{

        final Consumer<X> sub;

        public SimpleSubscriber(int group, int type, Consumer<X> sub) {
            super(group, type);
            this.sub = sub;
        }

        @Override
        public void onEvent(Object object) throws Exception {
            this.sub.accept((X) object);
        }
    }
}
