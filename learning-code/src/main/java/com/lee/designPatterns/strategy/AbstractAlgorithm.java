package com.lee.designPatterns.strategy;

public abstract class AbstractAlgorithm implements IAlgorithm{

    public abstract void sort(int[] arr);

    //可以用于一些算法格式的整理转换以供impl里面的类继承

    public boolean check(int[] arr) {
        if (arr.length == 0)
            return true;
        return false;
    }

    public void swap(int[] arr,int a, int b) {
        int temp;

        temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
