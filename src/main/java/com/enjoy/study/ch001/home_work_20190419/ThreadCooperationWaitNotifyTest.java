package com.enjoy.study.ch001.home_work_20190419;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * <b>Author</b>: 小果<br/>
 * <b>Date</b>: 2019/04/19 19:44<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 线程协作：生产者消费者作业
 * <b>Description</b>:
 * 采用多线程技术，例如wait/notify，设计实现一个符合生产者和消费者问题的程序，
 * 对某一个对象（枪膛）进行操作，其最大容量是20颗子弹，生产者线程是一个压入线程，它不断向枪膛中压入子弹，
 * 消费者线程是一个射出线程，它不断从枪膛中射出子弹。
 */
public class ThreadCooperationWaitNotifyTest {

    private final int SIZE = 20;
    private int NUMBER = 0;
    private final Object lock = new Object();
    // 弹匣
    private Stack<Bullet> box = new Stack<>();

    /**
     * 生产子弹、装弹线程
     */
    private class LoadingBulletThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Random random = new Random();
                    // 锁定弹匣
                    synchronized (lock) {
                        if (box.size() < SIZE) {
                            // 如果弹匣还有空余，则压入一颗子弹
                            Bullet bullet = new Bullet(NUMBER++);
                            box.push(bullet);
                            log(fetchingBoxBullets() + " <-------- " + bullet.getNumber() + " loading.");
                            lock.notifyAll();
                        } else {
                            log("box is full, now waiting for shooting.");
                            // 弹匣已满，随机等待一段时间再看看
                            lock.wait(random.nextInt(500));
                        }
                    }
                    Thread.sleep(random.nextInt(500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            log("LoadingBulletThread interrupted, left " + box.size() + " bullets");
        }
    }

    private String fetchingBoxBullets() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < box.size(); i++) {
            result.append("O");
        }
        for (int i = box.size(); i < SIZE; i++) {
            result.append("-");
        }
        return result.toString();
    }

    /**
     * 子弹发射线程
     */
    private class ShuttingBulletThread implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Random random = new Random();
                    synchronized (lock) {
                        if (box.size() > 0) {
                            // 如果弹匣内有子弹，则将最上面一颗发射出去
                            Bullet bullet = box.pop();
                            log(fetchingBoxBullets() + " --------> " + bullet.getNumber() + " shooting.");
                            bullet = null;
                            lock.notifyAll();
                        } else {
                            log("box is empty, now waiting for loading.");
                            // 弹匣为空，随机等待一段时间之后再看看
                            lock.wait(random.nextInt(500));
                        }
                    }
                    Thread.sleep(random.nextInt(500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            log("ShuttingBulletThread interrupted, left " + box.size() + " bullets");
        }
    }


    /**
     * 子弹类
     */
    private class Bullet {
        // 子弹编号
        private int number;
        // 子弹生产时间
        private long productTime;

        Bullet(int number) {
            this.number = number;
            productTime = System.currentTimeMillis();
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public long getProductTime() {
            return productTime;
        }

        public void setProductTime(long productTime) {
            this.productTime = productTime;
        }

        public long shooting() {
            return System.currentTimeMillis() - productTime;
            //log("bullet " + number + "'s whole life time: " + (System.currentTimeMillis() - productTime) + "ms");
        }
    }

    private static void log(String string) {
        System.out.println("[" + System.currentTimeMillis() + "] " + string);
    }

    @Test
    public void test() throws InterruptedException {
        Random random = new Random();
        ArrayList<Thread> loadings = new ArrayList<>();
        int size = random.nextInt(5) + 1;
        for (int i = 0; i < size; i++) {
            Thread thread = new LoadingBulletThread();
            loadings.add(thread);
            thread.start();
        }
        System.out.println(size + " loading threads.");

        ArrayList<Thread> shutting = new ArrayList<>();
        size = random.nextInt(5) + 1;
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(new ShuttingBulletThread());
            shutting.add(thread);
            thread.start();
        }
        System.out.println(size + " shutting threads.");

//        Thread produce = new LoadingBulletThread();
//        produce.start();
//        Thread customer = new Thread(new ShuttingBulletThread());
//        customer.start();

        Thread.sleep(10000);
        for (Thread thread : loadings) {
            thread.interrupt();
        }
        Thread.sleep(10000);
        for (Thread thread : shutting) {
            thread.interrupt();
        }
//        produce.interrupt();
//        customer.interrupt();
    }
}
