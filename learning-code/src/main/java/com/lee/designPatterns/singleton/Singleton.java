package com.lee.designPatterns.singleton;

import java.lang.reflect.Constructor;

public class Singleton {

    /* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
    private static Singleton singleton = null;

    Singleton() {
    }

    //Singleton one 毫无线程安全
    public Singleton getInstance() {
        if (singleton == null)
            singleton = new Singleton();
        return singleton;
    }

    //Singleton two synchronized关键字锁住的是这个对象，这样的用法，在性能上会有所下降，因为每次调用getInstance()，
    // 都要对对象上锁，事实上，只有在第一次创建对象的时候需要加锁，之后就不需要了
    public synchronized Singleton getInstanceSyn() {
        if (singleton == null)
            singleton = new Singleton();
        return singleton;
    }

    //Singleton three双重校验锁（ DCL：double-checked locking)
    public Singleton getSingletonSyn2() {
        if (singleton == null) {
            synchronized (singleton) {
                if (singleton == null)
                    //有可能jvm优先为singleton分配内存空间，但此时并没有实例化（执行new Singleton()）
                    //所以下一个线程进来时发现singleton不为空，直接return了一个尚未赋值的singleton,当使用时却没有被初始化
                    //解决办法:
                    //可以将instance声明为volatile，即 private volatile static Singleton instance
                    //在线程B读一个volatile变量后，线程A在写这个volatile变量之前，所有可见的共享变量的值都将立即变得对线程B可见。
                    singleton = new Singleton();
            }
        }
        return singleton;
    }

    //Singleton four内部类
    private static class SingletonFactory {
        private static Singleton singleton = new Singleton();
    }

    public static Singleton getInstanceInerClass() {
        return SingletonFactory.singleton;
    }
    //似乎静态内部类看起来已经是最完美的方法了，其实不是，可能还存在反射攻击或者反序列化攻击。且看如下代码：
    public static void main(String[] args) throws Exception {
        Singleton singleton = Singleton.getInstanceInerClass();
        Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Singleton newSingleton = constructor.newInstance();
        System.out.println(singleton == newSingleton);
    }

    //Singleton five 单独为创建同步
    public Singleton getInstanceSyn3() {
        if (singleton == null) {
            initInstance();
        }
        return singleton;
    }

    private synchronized void initInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
    }

    //如果对象被用于实例化，需保证对象在序列化前后保持一致,防止反序列化攻击
    public Object readResolve() {
        return singleton;
    }

    //补充：采用"影子实例"的办法为单例对象的属性同步更新
    public void updateProperties() {
        Singleton shadow = new Singleton();//影子实例
        //shadow.updateProperties...
    }

    //枚举
//    public enum Singleton {
//        INSTANCE;
//        public void get() {
//            System.out.println("");
//        }
//    }
}
