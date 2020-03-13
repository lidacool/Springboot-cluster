package com.lee.designPatterns.factory.factoryMethod;



/**
 *
 * 此乃工厂方法模式
 *
 * 真正的抽象工厂本意是为了解决 工厂方法模式创建的工厂类过多增加了系统的开销
 *
 * 所以抽象工厂考虑将一些相关的具体类组成一个“具体类族”，由同一个工厂来统一生产
 * https://blog.csdn.net/zyhlwzy/article/details/80707488
 *
 * */
public interface Listen {

    void  listenMusic();

}
