package com.enjoy.study.ch008.edu.app;

import com.enjoy.study.ch008.edu.ITaskProcessor;
import com.enjoy.study.ch008.edu.PendingJobPool;
import com.enjoy.study.ch008.edu.internal.TaskResult;
import com.enjoy.study.ch008.edu.internal.TaskResultType;

import java.util.List;
import java.util.Random;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 22:30<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 测试<br/>
 * <b>Description</b>: 测试
 */
public class AppTest {

    private static final String JOB_NAME = "TEST";
    private static final int JOB_LEN = 1000;

    /**
     * 模拟实现任务处理器
     */
    private static class JobTask implements ITaskProcessor<Integer, Integer> {

        @Override
        public TaskResult<Integer> execute(Integer integer) {
            Random random = new Random();
            int flag = random.nextInt(500);
            try {
                Thread.sleep(flag);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (flag <= 400) {
                Integer returnValue = integer + flag;
                return new TaskResult<>(TaskResultType.Success, returnValue, "");
            } else if (flag > 401 && flag <= 450) {
                return new TaskResult<>(TaskResultType.Failure, -1, "task execute failure");
            } else {
                try {
                    throw new RuntimeException("Exception happens.");
                } catch (Exception e) {
                    return new TaskResult<>(TaskResultType.Exception, -1, e.getMessage());
                }
            }
        }
    }

    private static class QueryResult implements Runnable {

        private PendingJobPool pool;

        public QueryResult(PendingJobPool pendingJobPool) {
            super();
            this.pool = pendingJobPool;
        }

        @Override
        public void run() {
            int i = 0;
            while (i < 1000) {
                if (pool.ContainsJob(JOB_NAME)) {
                    List<TaskResult<String>> list = pool.getTaskDetail(JOB_NAME);
                    if (!list.isEmpty()) {
                        System.out.println(pool.getJobProgress(JOB_NAME));
                        System.out.println(list);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                i++;
            }
        }
    }

    public static void main(String[] args) {
        JobTask task = new JobTask();
        PendingJobPool pool = PendingJobPool.getInstance();
        pool.registerJob(JOB_NAME, JOB_LEN, task, 10);
        Random random = new Random();
        for (int i = 0; i < JOB_LEN; i++) {
            pool.putTask(JOB_NAME, random.nextInt(1000));
        }
        Thread thread = new Thread(new QueryResult(pool));
        thread.start();
    }
}
