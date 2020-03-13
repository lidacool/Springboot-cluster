package com.lee.designPatterns.templateMethod;

public abstract class AbstractCalculate {

    abstract public void sortOverride(int[] arr);

    public final void sort(int[] arr) {
        sortOverride(arr);
    }

    protected void swap(int[] arr, int a, int b) {
        int temp;
        temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    protected boolean check(int[] arr) {
        if (arr.length == 0)
            return true;
        return false;
    }

}
