package com.lee.invokeInterface;

import com.lee.util.random.RandomUtil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * java 并发实现
 * */
public class LatchTest {

    public static void main(String[] args) throws InterruptedException {

        Runnable taskTemp = new Runnable() {

            private int iCounter;// CountDownLatch 下 线程不安全

            @Override
            public void run() {
                // 发起请求
//              HttpClientOp.doGet("https://www.baidu.com/");
                iCounter++;
                System.out.println(System.nanoTime() + " [" + Thread.currentThread().getName() + "] iCounter = " + iCounter);
                int id = 1000000 + RandomUtil.randomIntCon(2, 4);
                System.out.println("用户：" + id + "协助一个鞭炮！");
                ToInterface.excu(id);
            }
        };

        LatchTest latchTest = new LatchTest();
//        latchTest.startTaskAllInOnce(5, taskTemp);
        latchTest.startNThreadsByBarrier(5, taskTemp);
    }

    /**
     * java闭锁 CountDownLatch 实现并发请求
     * */
    public long startTaskAllInOnce(int threadNums, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(threadNums);
        for(int i = 0; i < threadNums; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        // 使线程在此等待，当开始门打开时，一起涌入门中
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            // 将结束门减1，减到0时，就可以开启结束门了
                            endGate.countDown();
                        }
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            };
            t.start();
        }
        long startTime = System.nanoTime();
        System.out.println(startTime + " [" + Thread.currentThread() + "] All thread is ready, concurrent going...");
        // 因开启门只需一个开关，所以立马就开启开始门
        startGate.countDown();
        // 等等结束门开启
        endGate.await();
        long endTime = System.nanoTime();
        System.out.println(endTime + " [" + Thread.currentThread() + "] All thread is completed.");
        return endTime - startTime;
    }

    /**
     * 栅栏 CyclicBarrier, 也是提供一个等待所有线程到达某一点后，再一起开始某个动作
     * */
    public void startNThreadsByBarrier(int threadNums, Runnable finishTask) throws InterruptedException {
        // 设置栅栏解除时的动作，比如初始化某些值
        CyclicBarrier barrier = new CyclicBarrier(threadNums, finishTask);
        // 启动 n 个线程，与栅栏阀值一致，即当线程准备数达到要求时，栅栏刚好开启，从而达到统一控制效果
        for (int i = 0; i < threadNums; i++) {
            Thread.sleep(100);
            new Thread(new CounterTask(barrier)).start();
        }
        System.out.println(Thread.currentThread().getName() + " out over...");
    }

    class CounterTask implements Runnable {

        // 传入栅栏，一般考虑更优雅方式
        private CyclicBarrier barrier;

        public CounterTask(final CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        public void run() {
            System.out.println(Thread.currentThread().getName() + " - " + System.currentTimeMillis() + " is ready...");
            try {
                // 设置栅栏，使在此等待，到达位置的线程达到要求即可开启大门
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " - " + System.currentTimeMillis() + " started...");
        }
    }
}
