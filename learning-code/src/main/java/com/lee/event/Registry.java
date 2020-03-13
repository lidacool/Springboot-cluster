package com.lee.event;

import com.lee.event.util.LambdaFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public final class Registry {

    private static Map<Class<?>, Declaring[]> declaringMap = new HashMap<>();
    private static Map<Class<?>, Integer> groupMap = new HashMap<>();
    private static AtomicInteger _group = new AtomicInteger();


    public static void regist(Object object, Contain contain) {
        Declaring[] declarings = get(object.getClass());

        for (Declaring declaring : declarings) {
            contain.regist(declaring.newSubscriber(object));
        }
    }

    private static Declaring[] get(Class<?> aClass) {
        Declaring[] declarings = declaringMap.get(aClass);
        return declarings == null ? getSafely(aClass) : declarings;
    }

    private static synchronized Declaring[] getSafely(Class<?> aClass) {
        Declaring[] declarings = declaringMap.get(aClass);
        if (declarings == null) {
            declarings = build(aClass);
            declaringMap.put(aClass, declarings);
        }
        return declarings;
    }

    private static Declaring[] build(Class<?> aClass) {
        int group = getGroup(aClass);
        return Arrays.stream(aClass.getMethods())

                .filter(method -> method.isAnnotationPresent(Subscribe.class)
                        && !Void.class.isAssignableFrom(getOnlyParamType(method)))

                .map(method -> new Declaring(group, method))

                .toArray(s -> new Declaring[s]);

    }

    private static Class<?> getOnlyParamType(Method method) {
        Class<?>[] types = method.getParameterTypes();
        return types.length == 1 ? types[0] : Void.class;
    }

    private static int getGroup(Class<?> aClass) {
        Integer group = groupMap.get(aClass);
        return group == null ? getGroupSafely(aClass) : group.intValue();
    }

    private static int getGroupSafely(Class<?> aClass) {
        Integer group = groupMap.get(aClass);
        if (group != null) {
            return group.intValue();
        }

        int g = _group.incrementAndGet();
        groupMap.put(aClass, g);
        return g;
    }


    static class Declaring {//声明
        final int group;
        final int type;
        final BiConsumer invoker;

        public Declaring(int group, Method method) {
            try {
                method.setAccessible(true);
                this.group = group;
                this.type = checkAndGetEventType(method);
                this.invoker = LambdaFactory.create(BiConsumer.class, method);
            } catch (Throwable throwable) {
                throw new IllegalArgumentException(throwable);
            }

        }

        private int checkAndGetEventType(Method method) {
            if (method.getParameterTypes().length != 1 || method.getParameterTypes()[0].getAnnotation(Event.class) == null) {
                throw new IllegalArgumentException("事件订阅方法必须是一个参数!");
            }
            return method.getParameterTypes()[0].getAnnotation(Event.class).value();
        }

        public Subscriber newSubscriber(Object object) {
            return new DeclaringSubscriber(this, object);
        }
    }

    static class DeclaringSubscriber extends Subscriber {
        final Object declareObj;
        final BiConsumer invoker;

        public DeclaringSubscriber(Declaring declaring, Object declareObj) {
            super(declaring.group, declaring.type);
            this.declareObj = declareObj;
            this.invoker = declaring.invoker;
        }

        @Override
        public void onEvent(Object object) throws Exception {
            invoker.accept(declareObj, object);
        }
    }
}
