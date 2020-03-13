package com.lee.designPatterns.templateMethod.inherited;

import com.lee.designPatterns.templateMethod.AbstractCalculate;

public class Bubble extends AbstractCalculate {

    @Override
    public void sortOverride(int[] arr) {

        if (check(arr))
            return;

        int len = arr.length;

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - i - 1; j++) {
                if (arr[j] > arr[j + 1])
                    swap(arr, j, j + 1);
            }
        }

    }

}
