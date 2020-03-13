package com.lee.designPatterns.builder.factory;

import com.lee.designPatterns.builder.Travel;
import com.lee.designPatterns.builder.impl.BicycleTravel;
import com.lee.designPatterns.builder.impl.CarTravel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Builder {

    private static final String car_travel="carTravel";
    private static final String bicycle_travel="bicycleTravel";

    Map<String,List<Travel>> map=new HashMap<>();

    public void  produceCar(int count){
        List<Travel> carList=map.get(car_travel);
        if (carList==null)
            carList=new ArrayList<>();
        int n=count;
        for (int i = 0; i < n; i++) {
            carList.add(new CarTravel());
        }
        map.put(car_travel,carList);
    }

    public void produceBicycle(int count){
        List<Travel> bicycleList=map.get(bicycle_travel);
        if (bicycleList==null)
            bicycleList=new ArrayList<>();
        int n=count;
        for (int i = 0; i < n; i++) {
            bicycleList.add(new BicycleTravel());
        }
        map.put(bicycle_travel,bicycleList);
    }

    public Map<String, List<Travel>> getMap() {
        return map;
    }
}
