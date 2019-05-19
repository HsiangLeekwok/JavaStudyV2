package com.enjoy.study.ch008.edu;


import com.enjoy.study.ch008.edu.internal.JobInfo;
import com.enjoy.study.ch008.edu.internal.TaskResult;
import com.enjoy.study.ch008.edu.internal.TaskResultType;

import java.util.List;
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

    // 线程数量
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    // 任务队列，有界队列
    private static BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(5000);
    // 线程池，固定大小，有界队列
    private static ExecutorService taskExecutor = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 60, TimeUnit.SECONDS, taskQueue);

    // 工作列表
    private static ConcurrentHashMap<String, JobInfo<?>> jobInfoMap = new ConcurrentHashMap<>();

    /**
     * 获取当前正在进行的工作列表
     *
     * @return 返回当前正在执行的任务列表
     */
    Map<String, JobInfo<?>> getMap() {
        return jobInfoMap;
    }

    // 单例模式启动
    private PendingJobPool() {

    }

    private static class InstanceHolder {
        private static PendingJobPool instance = new PendingJobPool();
    }

    /**
     * 获取工作线程池单例对象
     *
     * @return
     */
    public static PendingJobPool getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * 对用户提交的任务进行包装，提交给线程池使用，并将处理任务的结果写入缓存共查询
     *
     * @param <Data>
     * @param <Result>
     */
    private static class PendingTask<Data, Result> implements Runnable {

        private JobInfo<Result> jobInfo;
        private Data processData;

        PendingTask(JobInfo<Result> jobInfo, Data processData) {
            this.jobInfo = jobInfo;
            this.processData = processData;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            Result r = null;
            ITaskProcessor<Data, Result> processor = (ITaskProcessor<Data, Result>) jobInfo.getTaskProcessor();
            TaskResult<Result> result = null;
            try {
                result = processor.execute(processData);
                if (null == result) {
                    result = new TaskResult<>(TaskResultType.Exception, null, "result is null");
                }
                // 获取用户任务执行的返回值
                r = result.getReturnValue();
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
     * @param jobName  任务归属的工作
     * @param data     任务执行所必需的数据
     * @param <Data>
     * @param <Result>
     */
    public <Data, Result> void putTask(String jobName, Data data) {
        JobInfo<Result> jobInfo = getJob(jobName);
        if (null == jobInfo) {
            throw new RuntimeException("Please register job \"" + jobName + "\" first.");
        }
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
    public <Result> void registerJob(String jobName, int jobLength, ITaskProcessor<?, ?> processor, int expireTime) {
        JobInfo<Result> jobInfo = new JobInfo<>(jobName, jobLength, processor, expireTime);
        if (null != jobInfoMap.putIfAbsent(jobName, jobInfo)) {
            throw new RuntimeException(jobName + " is already registered.");
        }
    }

    @SuppressWarnings("unchecked")
    private <Result> JobInfo<Result> getJob(String jobName) {
        JobInfo<Result> jobInfo = (JobInfo<Result>) jobInfoMap.get(jobName);
        if (null == jobInfo) {
            throw new RuntimeException("No job names \"" + jobName + "\".");
        }
        return jobInfo;
    }

    /**
     * 线程池中是否有指定名称的工作
     *
     * @param jobName 工作名
     * @return
     */
    public boolean ContainsJob(String jobName) {
        return null != getJob(jobName);
    }

    /**
     * 查询工作进度
     *
     * @param jobName
     * @param <Result>
     * @return
     */
    public <Result> String getJobProgress(String jobName) {
        JobInfo<Result> jobInfo = getJob(jobName);
        return jobInfo.getProgress();
    }

    /**
     * 查询任务的执行结果
     *
     * @param jobName
     * @param <Result>
     * @return 返回工作中已经执行了的任务结果
     */
    public <Result> List<TaskResult<Result>> getTaskDetail(String jobName) {
        JobInfo<Result> jobInfo = getJob(jobName);
        return jobInfo.getTaskDetail();
    }
}
