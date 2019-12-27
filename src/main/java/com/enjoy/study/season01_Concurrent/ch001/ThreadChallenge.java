package com.enjoy.study.season01_Concurrent.ch001;

import java.util.Random;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2019/12/27 09:04<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class ThreadChallenge {

    private static int line = 10;

    private static class Tool extends Thread {

        Tool(String name) {
            super(name);
        }

        @Override
        public void run() {
            line++;
            if (line == 13) {
                System.out.println(this.getName());
            }
        }
    }

    public static void main(String[] args) {

        new Tool("car").start();

        Tool bike = new Tool("bike");
        bike.setPriority(Thread.MAX_PRIORITY);
        bike.setDaemon(false);
        bike.start();

        Tool train = new Tool("train");
        train.setPriority(Thread.MIN_PRIORITY);
        train.start();
    }
}
