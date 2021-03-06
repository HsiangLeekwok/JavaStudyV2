package com.enjoy.study.season01_Concurrent.ch008.edu;

import com.enjoy.study.season01_Concurrent.ch008.edu.internal.DelayItem;
import com.enjoy.study.season01_Concurrent.ch008.edu.internal.JobInfo;

import java.util.Map;
import java.util.concurrent.DelayQueue;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 21:29<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class JobCheckProcessor {

    private static DelayQueue<DelayItem<String>> queue = new DelayQueue<>();

    // 单例化
    private static class ProcessorHolder {
        static JobCheckProcessor instance = new JobCheckProcessor();
    }

    public static JobCheckProcessor getInstance() {
        return ProcessorHolder.instance;
    }

    /**
     * 处理到期的任务
     */
    private static class FetchingJob implements Runnable {
        // 缓存的工作信息
        private static Map<String, JobInfo<?>> jobInfoMap = PendingJobPool.getInstance().getMap();

        @Override
        public void run() {
            while (true) {
                try {
                    DelayItem<String> item = queue.take();
                    String jobName = item.getData();
                    jobInfoMap.remove(jobName);
                    System.out.println(jobName + " is timeout.");
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }
        }
    }

    /**
     * 任务处理完之后，加入定时缓存中，提供一段时间内的缓存查询，超时之后再清除
     *
     * @param jobName    工作名称
     * @param expireTime 超时时间
     */
    public void putJob(String jobName, long expireTime) {
        DelayItem<String> item = new DelayItem<>(expireTime, jobName);
        queue.offer(item);
        System.out.println(jobName + " has putting in expire queue, expire time: " + expireTime);
    }

    static {
        Thread thread = new Thread(new FetchingJob());
        // 守护线程
        thread.setDaemon(true);
        thread.start();
        System.out.println("Job expire check processor is started...... ");
    }
}
