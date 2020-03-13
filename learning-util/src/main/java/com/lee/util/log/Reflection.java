package com.lee.util.log;

public class Reflection extends SecurityManager {

    static final Reflection relection = new Reflection();

    @Override
    protected Class<?>[] getClassContext() {
        return super.getClassContext();
    }

    public static Class<?> getCallerClass() {
        return getCallerClass(2);
    }

    public static Class<?> getCallerClass(int depth) {
        Class<?>[] classes = relection.getClassContext();
        int index = depth + 2;
        int len = classes.length;
        return len > index ? classes[index] : classes[len - 1];
    }

    public static Class<?> getCallerClass(final Class<?> anchor){////
        boolean next=false;
        for (final Class<?> clazz: relection.getClassContext()) {
            if (clazz.equals(anchor)){
                next=true;
                continue;
            }
            if (next) return clazz;
        }
        return Object.class;
    }
}
