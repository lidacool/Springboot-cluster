package com.lee.designPatterns.strategy.impl;

import com.lee.designPatterns.strategy.AbstractAlgorithm;
import com.lee.designPatterns.strategy.IAlgorithm;

public class Insertion extends AbstractAlgorithm{

    //小规模数据，基本有序十分高校
    //循环次数(n-1)*x
    //交换次数x
    @Override
    public void sort(int[] arr) {
        if (check(arr))
            return;

        int len = arr.length;

        for (int i = 0; i < len-1; i++) {
            insert(arr,1,i+1);
        }
    }

    public void  insert(int[] arr,int gap,int insert_index){
        int insert_value = arr[insert_index];

        int pre_index = insert_index-gap;

        while (pre_index >= 0 && arr[pre_index] > insert_value) {
            arr[pre_index + gap] = arr[pre_index];
            pre_index -=gap;
        }
        if (pre_index+gap!=insert_index){
            arr[pre_index + gap] = insert_value;
        }
    }
}
