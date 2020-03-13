package com.lee.designPatterns.observer.test;

import com.lee.designPatterns.observer.MySubject;
import com.lee.designPatterns.observer.Subject;
import com.lee.designPatterns.observer.observers.impl.Observer1;
import com.lee.designPatterns.observer.observers.impl.Observer2;

public class TestObserver {

    public static void main(String[] args) {
        Subject subject=new MySubject();

        subject.add(new Observer1());
        subject.add(new Observer2());
        subject.operation();
    }
}
