package com.lee.designPatterns.observer.observers.impl;

import com.lee.designPatterns.observer.observers.Observer;

public class Observer2 implements Observer {
    @Override
    public void listenUpdate() {
        System.out.println("observer--2 listen the update");
    }
}
