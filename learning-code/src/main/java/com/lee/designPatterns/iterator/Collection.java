package com.lee.designPatterns.iterator;


public interface Collection<T> {

     Iterator iterator();

     Object get(int index);

     int size();

     void add(Object object);

}
