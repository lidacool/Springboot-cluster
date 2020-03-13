package com.lee;

import org.springframework.context.ApplicationContext;


/**
 * 将web的启动 ApplicationContext 上浮
 * 用于某些业务逻辑
 * eg：timer
 */
public class InjectUtil {

    private static ApplicationContext ac;//cache start ApplicationContext

    public static void init(ApplicationContext ac0) {
        ac = ac0;
    }

    public static <T> T getBean(Class<T> clazz) {
        return ac.getBean(clazz);
    }

    public static <T> T getBean(String name) {
        return (T) ac.getBean(name);
    }
}
