package com.lee.designPatterns.observer;

public class MySubject extends AbstractSubject {


    @Override
    public void operation() {
        System.out.println("begin listen！");
        notifyObservers();
    }
}
