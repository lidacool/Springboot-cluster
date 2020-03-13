package com.lee.designPatterns.strategy.test;

import com.lee.designPatterns.strategy.IAlgorithm;
import com.lee.designPatterns.strategy.impl.Quick;

public class TestStrategy {

    public static void main(String[] args) {
        IAlgorithm iAlgorithm=new Quick();
        int[] arr={5,4,1,2,3,7};
        iAlgorithm.sort(arr);

        for (int i = 0; i < arr.length; i++) {

            System.out.println(arr[i]);
        }
    }
}
