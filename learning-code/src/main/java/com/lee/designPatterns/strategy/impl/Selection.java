package com.lee.designPatterns.strategy.impl;

import com.lee.designPatterns.strategy.AbstractAlgorithm;
import com.lee.designPatterns.strategy.IAlgorithm;

public class Selection extends AbstractAlgorithm implements IAlgorithm {

    //循环次数n*(1+n)/2
    //交换次数n
    @Override
    public void sort(int[] arr) {
        if (check(arr))
            return;

        int len=arr.length;

        for (int i = 0; i < len; i++) {
            int min_index=i;
            for (int j = i; j < len; j++) {
                if (arr[j]<arr[min_index]){
                    min_index=j;
                }
            }
            if (i!=min_index){
                swap(arr,i,min_index);
            }
        }

    }
}
