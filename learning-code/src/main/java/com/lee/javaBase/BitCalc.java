package com.lee.javaBase;

import java.util.Random;

public class BitCalc {

    public static void main(String[] args) {
//        test1();
//        test2();

        //java规范中描述 int a<<b, b这个操作数只能取二进制数的低五位（就是最后5位）
        //b操作数取后五位，也可以理解成 b/32 取余数
        //所以 int a << b 可以理解成 int a << (b%32)
        //System.out.println(1<<32);//所以相当于不移动
//        System.out.println(1<<31);//结果为-2147483648，因为1移动到了最高位影响到了符号位，实际已经溢出

        //为什么int最小值-2147483648而最大值却是2147483647
        //因为计算机是以补码的形式来存储数字的，不管-0还是+0，补码都是0000 0000 0000 0000，
        //这就造成了没有任何一个数的补码是1000 0000 0000 0000，所以就可以把这个补码用来存储一个数（为了不不浪费资源），就规定用它来存储-(int_max+1)，所以int最小值是-2147483648，即 1000 0000 0000 0000。
//        int x = 0x7fffffff;
//        System.out.println(-x-1);
//        System.out.println(-x-2);

        //标签
//        test3();
        tstt4();

    }

    private static void tstt4() {
        int i = 0;
        outer:
        while (true) {
            prt("Outer while loop");
            while (true) {
                i++;
                prt("i = " + i);
                if (i == 1) {
                    prt("continue");
                    continue;
                }
                if (i == 3) {
                    prt("continue outer");
                    continue outer;
                }
                if (i == 5) {
                    prt("break");
                    break;
                }
                if (i == 7) {
                    prt("break outer");
                    break outer;
                }
            }
        }
    }

    public static void test3() {
        int i = 0;
        outer:
// Can't have statements here
        for (; true; ) { // infinite loop
            inner:
            // Can't have statements here
            for (; i < 10; i++) {
                prt("i = " + i);
                if (i == 2) {
                    prt("continue");
                    continue;
                }
                if (i == 3) {
                    prt("break");
                    i++; // Otherwise i never
                    // gets incremented.
                    break;
                }
                if (i == 7) {
                    prt("continue outer");
                    i++; // Otherwise i never
                    // gets incremented.
                    continue outer;
                }
                if (i == 8) {
                    prt("break outer");
                    break outer;
                }
                for (int k = 0; k < 5; k++) {
                    if (k == 3) {
                        prt("continue inner");
                        continue inner;
                    }
                }
            }
        }     // Can't break or continue
        // to labels here
    }

    static void prt(String s) {
        System.out.println(s);
    }

    private static void test2() {
        Random rand = new Random();
        int i = rand.nextInt();
        int j = rand.nextInt();
        pBinInt("-1", -1);
        pBinInt("+1", +1);
        int maxpos = 2147483647;
        pBinInt("maxpos", maxpos);
        int maxneg = -2147483648;
        pBinInt("maxneg", maxneg);
        pBinInt("i", i);
        pBinInt("~i", ~i);
        pBinInt("-i", -i);
        pBinInt("j", j);
        pBinInt("i & j", i & j);
        pBinInt("i | j", i | j);
        pBinInt("i ^ j", i ^ j);
        pBinInt("i << 5", i << 5);
        pBinInt("i >> 5", i >> 5);
        pBinInt("(~i) >> 5", (~i) >> 5);
        pBinInt("i >>> 5", i >>> 5);
        pBinInt("(~i) >>> 5", (~i) >>> 5);

        long l = rand.nextLong();
        long m = rand.nextLong();
        pBinLong("-1L", -1L);
        pBinLong("+1L", +1L);
        long ll = 9223372036854775807L;
        pBinLong("maxpos", ll);
        long lln = -9223372036854775808L;
        pBinLong("maxneg", lln);
        pBinLong("l", l);
        pBinLong("~l", ~l);
        pBinLong("-l", -l);
        pBinLong("m", m);
        pBinLong("l & m", l & m);
        pBinLong("l | m", l | m);
        pBinLong("l ^ m", l ^ m);
        pBinLong("l << 5", l << 5);
        pBinLong("l >> 5", l >> 5);
        pBinLong("(~l) >> 5", (~l) >> 5);
        pBinLong("l >>> 5", l >>> 5);
        pBinLong("(~l) >>> 5", (~l) >>> 5);
    }

    static void pBinInt(String s, int i) {
        System.out.println(s + ", int: " + i + ", binary: ");
        System.out.print("   ");
        for (int j = 31; j >= 0; j--)
            if (((1 << j) & i) != 0) System.out.print("1");
            else System.out.print("0");
        System.out.println();
    }

    static void pBinLong(String s, long l) {
        System.out.println(s + ", long: " + l + ", binary: ");
        System.out.print("   ");
        for (int i = 63; i >= 0; i--)
            if (((1L << i) & l) != 0) System.out.print("1");
            else System.out.print("0");
        System.out.println();
    }

    private static void test1() {
        int i = -1;
        i >>>= 10;
        System.out.println(i);
        long l = -1;
        l >>>= 10;
        System.out.println(l);
        short s = -1;
        s >>>= 10;
        System.out.println(s);
        byte b = -1;
        b >>>= 10;
        System.out.println(b);

        int k = 2;
        for (int j = 0; j < 21; j++) {
            k *= 2;
        }
        System.out.println(k);
    }
}
