package com.enjoy.study.season01_Concurrent.ch001;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/04/16 21:04<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Description</b>: 创建线程的方法
 */
public class _02_NewThread {

    private static class UseThread extends Thread {
        @Override
        public void run() {
            System.out.println("I'm extends thread.class.");
        }
    }

    private static class UseRunnable implements Runnable {
        public void run() {
            System.out.println("I'm implements Runnable.");
        }
    }

    public static void main(String[] args) {
        UseThread useThread = new UseThread();
        useThread.start();

        new Thread(new UseRunnable()).start();
    }
}
