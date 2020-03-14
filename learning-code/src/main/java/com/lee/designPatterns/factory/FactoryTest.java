package com.lee.designPatterns.factory;

import com.lee.designPatterns.factory.abstractFac.BlueShapeFactory;
import com.lee.designPatterns.factory.abstractFac.RedShapeFactory;
import com.lee.designPatterns.factory.abstractFac.ShapeFactory;
import com.lee.designPatterns.factory.abstractFac.divByLv.Circle;
import com.lee.designPatterns.factory.abstractFac.divByLv.Square;
import com.lee.designPatterns.factory.factoryMethod.Listen;
import com.lee.designPatterns.factory.factoryMethod.Provider;
import com.lee.designPatterns.factory.factoryMethod.impl.CallFactory;
import com.lee.designPatterns.factory.simple.SendFactory;
import com.lee.designPatterns.factory.simple.Sender;

public class FactoryTest {

    public static void main(String s[]) {
        abstractFactory();
    }

    /**
     * 抽象工厂模式
     * 抽象工厂本意是为了解决 工厂方法模式创建的工厂类过多增加了系统的开销
     * *
     * * 所以抽象工厂考虑将一些相关的具体类组成一个“具体类族”，由同一个工厂来统一生产
     * * https://blog.csdn.net/zyhlwzy/article/details/80707488
     * eg: 在有方圆棱三个形状的产品上 ， 增加红绿蓝3个颜色 需创建的工厂就很多
     * <p>
     * impl:将一个形状当做一个等级 一种颜色当成族
     *
     * 而抽象工厂模式进阶中已经干掉了抽象工厂类，去利用反射进行实例化。
     */
    private static void abstractFactory() {
        ShapeFactory shapeFactory = new RedShapeFactory();
        Circle circle = shapeFactory.createCircle();
        ShapeFactory shapeFactory2 = new BlueShapeFactory();
        Square square = shapeFactory2.createSquare();
        circle.draw();
        square.draw();
    }


    /**
     * 工厂方法模式完全符合面向对象设计的核心原则：开放-封闭原则。
     * 工厂方法模式投入比简单工厂模式要大。对比我们编码也是投入大，但是带来的可维护性是非常高的。
     * 工厂方法模式的使用对开发者来说更需要一定的抽象能力，没有这种能力之前，千万不要乱用（设计模式也是如此）。
     */
    private static void factoryMethod() {
        Provider abstractFactory = new CallFactory();
        Listen listen = abstractFactory.produce();
        listen.listenMusic();
    }

    private static void simpleFactory() {
        Sender sender = SendFactory.produceMailStatic();
        sender.sender();
    }

}
