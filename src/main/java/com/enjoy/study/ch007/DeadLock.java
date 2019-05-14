package com.enjoy.study.ch007;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/14 21:01<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class DeadLock {

    private static Object first = new Object();
    private static Object second = new Object();

    private static void first2Second() throws InterruptedException {
        String name = Thread.currentThread().getName();
        synchronized (first) {
            System.out.println(name + " got first");
            Thread.sleep(100);
            synchronized (second) {
                System.out.println(name + " got second");
            }
        }
    }

    private static void second2First() throws InterruptedException {
        String name = Thread.currentThread().getName();
        synchronized (second) {
            System.out.println(name + " got second");
            Thread.sleep(100);
            synchronized (first) {
                System.out.println(name + " got first");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    first2Second();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        second2First();
    }
}
