package com.lee.designPatterns.strategy.impl;

import com.lee.designPatterns.strategy.AbstractAlgorithm;
import com.lee.designPatterns.strategy.IAlgorithm;

public class Shell extends AbstractAlgorithm implements IAlgorithm {

    //先将数组分成两个数据一组，小组非常多，每个小组进行插入排序
    //然后乘数为2扩大小组里面的数据，减少小组数，
    //这样每次扩大的小组基本有序了，插入排序也就快起来了

    @Override
    public void sort(int[] arr) {

        if (check(arr))
            return;

        int len=arr.length;

        for (int gap = len/2; gap>0 ; gap/=2) {
            for (int j = gap; j < len; j++) {

                new Insertion().insert(arr,gap,j);

            }
        }

    }

}
