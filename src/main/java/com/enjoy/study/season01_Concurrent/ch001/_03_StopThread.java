package com.enjoy.study.season01_Concurrent.ch001;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/04/16 21:16<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Description</b>: 中断线程
 */
public class _03_StopThread {

    private static class Test extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }


    public static void main(String[] args) {
        Thread thread = new Thread(() -> {

        });
        thread.start();
    }
}
