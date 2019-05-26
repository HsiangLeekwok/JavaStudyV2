package com.enjoy.study.season01_Concurrent.ch004;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/04/30 10:20<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/> 测试自写的 FutureTask
 * <b>Description</b>:
 */
public class TestMyTask {


    private class TaskCallable implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            int count = 1000;
            int sum = 0;
            for (int i = 0; i < count; i++) {
                sum += i;
                Thread.sleep(1);
            }
            return sum;
        }
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        HomeWork20190430_CAS_FutureTask<Integer> task = new HomeWork20190430_CAS_FutureTask<>(new TaskCallable());
        Thread thread = new Thread(task);
        long start = System.currentTimeMillis();
        thread.start();
        new Thread(() -> {
            try {
                int res = task.get(2000, TimeUnit.MILLISECONDS);
                System.out.println("result(in thread): " + res + ", used time: " + (System.currentTimeMillis() - start) + "ms");
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }).start();
        int result = task.get();
        System.out.println("result: " + result + ", time used: " + (System.currentTimeMillis() - start) + "ms");
        //Thread.sleep(3000);
    }
}
