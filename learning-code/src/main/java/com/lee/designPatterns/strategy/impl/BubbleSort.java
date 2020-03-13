package com.lee.designPatterns.strategy.impl;

import com.lee.designPatterns.strategy.AbstractAlgorithm;
import com.lee.designPatterns.strategy.IAlgorithm;


/**
 * 有些类名在mybatis 映射实体时会重复mapper
 * 所以类名尽量具体详细点 保证少重复
 * */
public class BubbleSort extends AbstractAlgorithm implements IAlgorithm {

    //循环次数n*(1+n)/2
    //交换次数,逆序时交换n*(1+n)/2,顺序时0
    @Override
    public void sort(int[] arr) {
        if (check(arr))
            return;

        int len = arr.length;

        int temp;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (arr[j + 1] < arr[j]) {
                    swap(arr,j+1,j);
                }
            }
        }

    }
}
