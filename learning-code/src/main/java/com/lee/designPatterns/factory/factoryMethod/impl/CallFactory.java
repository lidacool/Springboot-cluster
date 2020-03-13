package com.lee.designPatterns.factory.factoryMethod.impl;


import com.lee.designPatterns.factory.factoryMethod.Listen;
import com.lee.designPatterns.factory.factoryMethod.Provider;

public class CallFactory implements Provider {

    @Override
    public Listen produce() {
        return new CallListen();
    }
}
