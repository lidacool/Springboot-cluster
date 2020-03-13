package com.lee.designPatterns.builder.impl;

import com.lee.designPatterns.builder.Travel;

public class CarTravel implements Travel {
    @Override
    public void travel() {
        System.out.println("i like drive car for travel myself");
    }
}
