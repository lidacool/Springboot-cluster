package com.lee.javaBase;

/**
 * GC finalize
 */
public class Chair {

    static boolean gcrun = false;
    static boolean f = false;
    static int created = 0;
    static int finalized = 0;
    int i;

    Chair() {
        i = ++created;
        if (created == 47) System.out.println("Created 47");
    }


    /**
     * Java 提供了一个名为 finalize() 的方法，可为我们的类定义它。
     * 在理想情况下，它的工作原理应该是这样的：一 旦垃圾收集器准备好释放对象占用的存储空间，它首先调用 finalize()，
     * 而且只有 在下一次垃圾收集过程中，才会真正回收对象的内存。
     * 所以如果使用 finalize()， 就可以在垃圾收集期间进行一些重要的清除或清扫工作
     * */
    public void finalize() {
        if (!gcrun) {    // The first time finalize() is called:
            gcrun = true;
            System.out.println("Beginning to finalize after " + created + " Chairs have been created");
        }
        if (i == 47) {
            System.out.println("Finalizing Chair #47, " + "Setting flag to stop Chair creation");
            f = true;
        }
        finalized++;
        if (finalized >= created) System.out.println("All " + finalized + " finalized");
    }

    static class Garbage {
        public static void main(String[] args) {    // As long as the flag hasn't been set,
            // make Chairs and Strings:
            while (!Chair.f) {
                new Chair();
                new String("To take up space");
            }
            System.out.println("After all Chairs have been created:\n" + "total created = "
                    + Chair.created + ", total finalized = "
                    + Chair.finalized);// Optional arguments force garbage
            // collection & finalization:
            if (args.length > 0) {
                if (args[0].equals("gc") || args[0].equals("all")) {
                    System.out.println("gc():");
                    System.gc();
                }
                if (args[0].equals("finalize") || args[0].equals("all")) {
                    System.out.println("runFinalization():");
                    System.runFinalization();
                }
            }
            System.out.println("bye!");
        }
    }
}
