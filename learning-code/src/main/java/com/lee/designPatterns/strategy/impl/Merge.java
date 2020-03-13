package com.lee.designPatterns.strategy.impl;

import com.lee.designPatterns.strategy.AbstractAlgorithm;
import com.lee.designPatterns.strategy.IAlgorithm;

public class Merge extends AbstractAlgorithm implements IAlgorithm {

    //时间复杂度无论好坏都是NlogN
    //空间复杂度N，最多需要存放N个数据
    @Override
    public void sort(int[] arr) {

        if (check(arr))
            return;

        int[] tempArr = new int[arr.length];
        divideSort(arr, tempArr, 0, arr.length - 1);

    }


    //分治
    private void divideSort(int[] arr, int[] tempArr, int start_index, int end_index) {
        if (start_index < end_index) {
            int center = (start_index + end_index) / 2;
            divideSort(arr, tempArr, start_index, center);

            divideSort(arr, tempArr, center + 1, end_index);

            mergeSort(arr, tempArr, start_index, center + 1, end_index);
        }

    }

    //归并
    private void mergeSort(int[] arr, int[] tempArr, int left_start, int right_start, int right_end) {

        int left_end = right_start - 1;
        int temp_start = left_start;
        int merge_count = right_end - left_start + 1;

        while (left_start <= left_end && right_start <= right_end) {
            if (arr[left_start] <= arr[right_start]) {
                tempArr[temp_start++] = arr[left_start++];
            } else {
                tempArr[temp_start++] = arr[right_start++];
            }
        }

        while (left_start <= left_end)
            tempArr[temp_start++] = arr[left_start++];

        while (right_start <= right_end)
            tempArr[temp_start++] = arr[right_start++];

        for (int i = 0; i < merge_count; i++, right_end--) {
            arr[right_end] = tempArr[right_end];
        }

    }
}
