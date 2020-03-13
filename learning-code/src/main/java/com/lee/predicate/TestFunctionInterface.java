package com.lee.predicate;

import java.util.Arrays;

public class TestFunctionInterface {

    public static void main(String[] args) {
        PredicateMatch predicate = PredicateMatch.add(Arrays.asList(1, 2, 3, 4, 5));
        if (predicate.test(6)) {
            System.out.println(predicate.getClass().getName());
        } else {
            System.out.println("nothing to matching!");
        }
    }
}
