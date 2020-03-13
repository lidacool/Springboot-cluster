package com.lee.designPatterns.iterator;

public interface Iterator {

    Object previous();

    Object next();

    Object first();

    boolean hasNext();

}
