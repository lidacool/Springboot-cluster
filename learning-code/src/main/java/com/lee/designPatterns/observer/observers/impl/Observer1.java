package com.lee.designPatterns.observer.observers.impl;

import com.lee.designPatterns.observer.observers.Observer;

public class Observer1 implements Observer {
    @Override
    public void listenUpdate() {
        System.out.println("observer--1 listen the update");
    }
}
