package com.enjoy.study.ch004;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Author</b>: 小果<br/>
 * <b>Date</b>: 2019/04/24 10:41<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 实现自增的 AtomicInteger<br/>
 * <b>Description</b>: 小明中了彩票享受生活去了，他遗留了一个类，是个计数器的功能，现在请你完成这个计数器。
 * 在这个类里，他已经完成了线程安全的get方法和compareAndSet()方法，请你用循环CAS机制完成它。
 * 完成后自行启动多个线程测试是否能正常工作。
 */
public class HomeWork20190424_HalfAtomicInteger {

    private static final int SIZE = 100;

    private AtomicInteger atomicI = new AtomicInteger(0);

    /*请完成这个递增方法*/
    public void increment() {
        while (true) {
            int oldValue = getCount();
            if (compareAndSet(oldValue, oldValue + 1)) {
                break;
            } else {
                System.out.println(Thread.currentThread().getName() + " compareAndSet fail...");
            }
        }
    }

    public int getCount() {
        return atomicI.get();
    }

    public boolean compareAndSet(int oldValue, int newValue) {
        return atomicI.compareAndSet(oldValue, newValue);
    }

    private static class TestThread extends Thread {

        private HomeWork20190424_HalfAtomicInteger half;
        CyclicBarrier barrier;
        CountDownLatch latch;

        TestThread(int index, HomeWork20190424_HalfAtomicInteger half, CyclicBarrier barrier, CountDownLatch latch) {
            setName("thread_" + index);
            this.half = half;
            this.barrier = barrier;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                latch.await();
                Random random = new Random();
                int times = SIZE;
                while (times > 0) {
                    half.increment();
                    times--;
                    sleep(random.nextInt(10));
                }
                System.out.println(getName() + " complete increment.");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        HomeWork20190424_HalfAtomicInteger half = new HomeWork20190424_HalfAtomicInteger();
        CyclicBarrier barrier = new CyclicBarrier(SIZE + 1, () -> System.out.println("test complete, result is: " + half.getCount()));
        CountDownLatch latch = new CountDownLatch(SIZE);
        for (int i = 0; i < SIZE; i++) {
            new TestThread(i, half, barrier, latch).start();
            if (latch.getCount() == 1) {
                System.out.println("All threads begin to increment....");
            }
            latch.countDown();
        }
        barrier.await();
        System.out.println("All thread has completed increment.");
    }
}
