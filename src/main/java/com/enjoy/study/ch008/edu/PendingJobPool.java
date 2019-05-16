package com.enjoy.study.ch008.edu;


import com.enjoy.study.ch008.edu.internal.JobInfo;
import com.enjoy.study.ch008.edu.internal.TaskResult;
import com.enjoy.study.ch008.edu.internal.TaskResultType;

import java.util.Map;
import java.util.concurrent.*;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 21:09<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 多线程任务执行框架主类<br/>
 * <b>Description</b>:
 */
public class PendingJobPool {

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(5000);
    // 线程池，固定大小，有界队列
    private static ExecutorService taskExecutor = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 60, TimeUnit.SECONDS, taskQueue);

    // 工作列表
    private static ConcurrentHashMap<String, JobInfo<?>> jobInfoMap = new ConcurrentHashMap<>();

    /**
     * 获取当前正在进行的工作列表
     *
     * @return
     */
    public Map<String, JobInfo<?>> getMap() {
        return jobInfoMap;
    }

    // 单例模式启动
    private PendingJobPool() {

    }

    private static class InstanceHolder {
        private static PendingJobPool instance = new PendingJobPool();
    }

    public static PendingJobPool getInstance() {
        return InstanceHolder.instance;
    }

    // 对中左中的任务进行包装，提交给线程池使用，并将处理任务的结果写入缓存共查询
    private static class PendingTask<Data, Result> implements Runnable {

        private JobInfo<Result> jobInfo;
        private Data processData;

        public PendingTask(JobInfo<Result> jobInfo, Data processData) {
            this.jobInfo = jobInfo;
            this.processData = processData;
        }

        @Override
        public void run() {
            Result r = null;
            ITaskProcessor<Data, Result> processor = (ITaskProcessor<Data, Result>) jobInfo.getTaskProcessor();
            TaskResult<Result> result = null;
            try {
                result = processor.execute(processData);
                if (null == result) {
                    result = new TaskResult<>(TaskResultType.Exception, r, "result is null");
                }
                if (null == result.getResultType()) {
                    if (null == result.getReason()) {
                        result = new TaskResult<>(TaskResultType.Exception, r, "reason is null");
                    } else {
                        result = new TaskResult<>(TaskResultType.Exception, r, "result is null, reason: " + result.getReason());
                    }
                }
            } catch (Exception e) {
                result = new TaskResult<>(TaskResultType.Exception, r, e.getMessage());
            } finally {
                jobInfo.addTaskResult(result);
            }
        }
    }

    /**
     * 添加任务
     *
     * @param jobName
     * @param data
     * @param <Data>
     * @param <Result>
     */
    public <Data, Result> void putTask(String jobName, Data data) {
        JobInfo<Result> jobInfo = getJob(jobName);
        PendingTask<Data, Result> task = new PendingTask<>(jobInfo, data);
        taskExecutor.execute(task);
    }

    /**
     * 注册任务，需要先注册任务，才能添加task
     *
     * @param jobName
     * @param jobLength
     * @param processor
     * @param expireTime
     * @param <Result>
     */
    public <Result> void registerJob(String jobName, int jobLength,
                                     ITaskProcessor<?, ?> processor, int expireTime) {
        JobInfo<Result> jobInfo = new JobInfo<>(jobName, jobLength, processor, expireTime);
        if (null != jobInfoMap.putIfAbsent(jobName, jobInfo)) {
            throw new RuntimeException(jobName + " is already registered.");
        }
    }

    private <Result> JobInfo<Result> getJob(String jobName) {
        JobInfo<Result> jobInfo = (JobInfo<Result>) jobInfoMap.get(jobName);
        if (null == jobInfo) {
            throw new RuntimeException(jobName + " is null");
        }
        return jobInfo;
    }
}
