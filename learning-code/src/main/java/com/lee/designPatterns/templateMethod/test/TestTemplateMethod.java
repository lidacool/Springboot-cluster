package com.lee.designPatterns.templateMethod.test;

import com.lee.designPatterns.templateMethod.AbstractCalculate;
import com.lee.designPatterns.templateMethod.inherited.Bubble;

public class TestTemplateMethod {

    public static void main(String[] args) {
        AbstractCalculate abstractCalculate=new Bubble();

        int[] testSort=new int[]{5,2,3,1,4,};

        abstractCalculate.sort(testSort);

        for (int i = 0; i < testSort.length; i++) {
            System.out.println(testSort[i]);
        }
    }
}
