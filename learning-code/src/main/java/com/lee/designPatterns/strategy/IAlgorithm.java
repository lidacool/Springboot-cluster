package com.lee.designPatterns.strategy;

/**
 * 策略模式：就是一个问题有好几种解法，我们选择其中一种就可以了
 * eg:string转Interger中的digit方法中用到的 CharacterData类及其子类
 */
public interface IAlgorithm {

    void sort(int[] arr);

}
