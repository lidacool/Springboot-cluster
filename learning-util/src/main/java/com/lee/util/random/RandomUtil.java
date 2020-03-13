package com.lee.util.random;


import java.util.concurrent.ThreadLocalRandom;

/**
 * notes by learning hutool
 */
public class RandomUtil {

    private static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    public static boolean randomBoolean() {
        return randomInt(2) == 0;
    }

    private static int randomInt(int limit) {
        return getRandom().nextInt(limit);
    }

    /*左闭右闭*/
    public static int randomIntCon(int min, int max) {
        int result = getRandom().nextInt((max - min + 1) << 5);
        return (result >> 5) + min;
    }


    /*左闭右开*/
    public static int randomInt(int min, int max) {
        return getRandom().nextInt(min, max);
    }


}
