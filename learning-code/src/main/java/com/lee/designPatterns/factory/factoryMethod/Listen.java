package com.lee.designPatterns.factory.factoryMethod;



/**
 * 工厂方法模式
 * 其核心结构有四个角色，分别是抽象工厂；具体工厂；抽象产品；具体产品
 *
 * interface Provider:抽象工厂 callFactory/radioFactory:具体工厂
 * interface Listen：抽象产品  callListen/radioListen:具体产品
 *
 * */
public interface Listen {

    void  listenMusic();

}
