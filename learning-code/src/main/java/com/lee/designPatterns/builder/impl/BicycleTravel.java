package com.lee.designPatterns.builder.impl;

import com.lee.designPatterns.builder.Travel;

public class BicycleTravel implements Travel {
    @Override
    public void travel() {
        System.out.println("i like drive bicycle to travel myself");
    }
}
