package com.lee.designPatterns.strategy.impl;

import com.lee.designPatterns.strategy.AbstractAlgorithm;
import com.lee.designPatterns.strategy.IAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class Quick extends AbstractAlgorithm implements IAlgorithm {

    //快速排序是一个原地排序算法，是一个不稳定的排序算法，因为其在数据交换过程中可能会改变相等元素的原始位置。
    //如果快速排序每次都将数据分成相等的两部分，则快排的时间复杂度和归并排序相同,
    // 如果有序情况下时间复杂度很糟糕：n^2
    @Override
    public void sort(int[] arr) {
        if (check(arr))
            return;


        //guava 类库工具方法
        //int[] intArray = {1, 2, 3, 4};
        //List<Integer> list = Ints.asList(intArray);
        List<Integer> list = new ArrayList<>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }

        List<Integer> sort_list = quick(list);

        for (int i = 0; i < sort_list.size(); i++) {
            arr[i] = sort_list.get(i);
        }
    }

    private List<Integer> quick(List<Integer> list) {

        int len = list.size();
        if (len <= 1)
            return list;

        List<Integer> min_list = new ArrayList<>();
        List<Integer> max_list = new ArrayList<>();
        List<Integer> center_list = new ArrayList<>();

        int center = len / 2;
        for (Integer i : list) {
            if (i < list.get(center)) {
                min_list.add(i);
            } else if (i > list.get(center)) {
                max_list.add(i);
            } else {
                center_list.add(i);
            }
        }

        quick(min_list);
        quick(max_list);

        list.clear();
        list.addAll(min_list);
        list.addAll(center_list);
        list.addAll(max_list);
        return list;
    }

}
