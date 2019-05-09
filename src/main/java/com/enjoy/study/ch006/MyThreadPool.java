package com.enjoy.study.ch006;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/09 21:20<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class MyThreadPool {

    // 基本线程数量
    private static int WORK_COUNT = 5;
    /**
     * 任务队列，阻塞方式
     */
    private final BlockingQueue<Runnable> taskQueue;
    // 工作线程列表
    private WorkThread[] workThreads;
    // 实际的工作线程数量
    private final int work_number;

    public MyThreadPool() {
        this(WORK_COUNT, WORK_COUNT);
    }

    public MyThreadPool(int task_count, int work_number) {
        this.taskQueue = new ArrayBlockingQueue<>(task_count);
        this.work_number = work_number < 0 ? WORK_COUNT : work_number;
        workThreads = new WorkThread[work_number];
        // 工作线程已经准备好
        for (int i = 0; i < work_number; i++) {
            workThreads[i] = new WorkThread();
            workThreads[i].start();
        }
    }

    /**
     * 销毁线程池
     */
    public void destroy() {
        System.out.println("prepare to close pool.....");
        for (int i = 0; i < work_number; i++) {
            workThreads[i].stopWork();
            workThreads[i] = null;
        }
        taskQueue.clear();
    }

    /**
     * 放入任务，只是加入队列并不会立即执行
     *
     * @param task 任务
     */
    public void execute(Runnable task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MyThreadPool{}";
    }

    // 内部类，工作线程的实现
    private class WorkThread extends Thread {

        @Override
        public void run() {
            Runnable r;

            while (!isInterrupted()) {
                try {
                    r = taskQueue.take();
                    if (null != r) {
                        System.out.println(getId() + " ready execute...." + ((WorkTask) r).getName());
                        r.run();
                        r = null;
                    }
                } catch (Exception ignore) {

                }
            }
        }

        /**
         * 停止工作
         */
        public void stopWork() {
            interrupt();
        }
    }

    private class WorkTask implements Runnable {

        private String name;

        public WorkTask(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public void run() {
            try {
                Random random = new Random();
                Thread.sleep(random.nextInt(10) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
