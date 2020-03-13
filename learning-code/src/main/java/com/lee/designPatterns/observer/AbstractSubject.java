package com.lee.designPatterns.observer;

import com.lee.designPatterns.observer.observers.Observer;

import java.util.Enumeration;
import java.util.Vector;

public abstract  class AbstractSubject implements Subject {

    private Vector<Observer> observers=new Vector<Observer>();

    @Override
    public void add(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void del(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        Enumeration<Observer> enumeration=observers.elements();
        while (enumeration.hasMoreElements()){
            enumeration.nextElement().listenUpdate();
        }
    }

}
