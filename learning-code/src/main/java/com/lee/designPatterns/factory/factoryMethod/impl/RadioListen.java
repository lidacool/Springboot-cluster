package com.lee.designPatterns.factory.factoryMethod.impl;


import com.lee.designPatterns.factory.factoryMethod.Listen;

public class RadioListen implements Listen {
    @Override
    public void listenMusic() {
        System.out.println("this is a radio play music");
    }
}
