package com.lee.designPatterns.builder.test;

import com.lee.designPatterns.builder.Travel;
import com.lee.designPatterns.builder.factory.Builder;

import java.util.List;
import java.util.Map;

public class TestBuilderPattern {

    public static void main(String[] args) {
        Builder builder=new Builder();
        builder.produceCar(1);
        builder.produceBicycle(1);
        Map<String, List<Travel>> map=builder.getMap();

        for (List<Travel> travels:
             map.values()) {
            for (Travel travel:travels
                 ) {
                travel.travel();
            }

        }

    }
}
