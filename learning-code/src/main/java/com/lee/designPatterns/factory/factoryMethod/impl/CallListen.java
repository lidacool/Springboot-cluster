package com.lee.designPatterns.factory.factoryMethod.impl;


import com.lee.designPatterns.factory.factoryMethod.Listen;

public class CallListen implements Listen {
    @Override
    public void listenMusic() {
        System.out.println("this is a call play music");
    }
}
