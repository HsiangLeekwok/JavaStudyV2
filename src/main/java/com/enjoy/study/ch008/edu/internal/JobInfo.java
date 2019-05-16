package com.enjoy.study.ch008.edu.internal;

import com.enjoy.study.ch008.edu.ITaskProcessor;
import com.enjoy.study.ch008.edu.JobCheckProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 21:02<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 任务抽象类<br/>
 * <b>Description</b>:
 */
public class JobInfo<Result> {

    /**
     * 任务名称
     */
    private final String jobName;
    // 任务长度
    private final int jobLength;
    private final ITaskProcessor<?, ?> taskProcessor;

    private AtomicInteger successCount;
    private AtomicInteger proceedCount;

    // 存放每个任务的处理结果
    private LinkedBlockingDeque<TaskResult<Result>> taskQueue;
    private final long expireTime;

    private static JobCheckProcessor processor = JobCheckProcessor.getInstance();

    public JobInfo(String jobName, int jobLength, ITaskProcessor<?, ?> processor, long expireTime) {
        this.jobLength = jobLength;
        this.jobName = jobName;
        successCount = new AtomicInteger(0);
        proceedCount = new AtomicInteger(0);
        this.taskProcessor = processor;
        taskQueue = new LinkedBlockingDeque<>(this.jobLength);
        this.expireTime = expireTime;
    }

    public ITaskProcessor<?, ?> getTaskProcessor() {
        return taskProcessor;
    }

    public int getSuccessCount() {
        return successCount.get();
    }

    public int getProceedCount() {
        return proceedCount.get();
    }

    public int getJobLength() {
        return jobLength;
    }

    // 提供工作中每个任务的处理结果
    public List<TaskResult<Result>> getTaskDetail() {
        List<TaskResult<Result>> list = new ArrayList<>();
        TaskResult<Result> result;
        while ((result = taskQueue.pollFirst()) != null) {
            list.add(result);
        }
        return list;
    }

    /**
     * 任务执行结果放入队列；
     * 从业务应用角度来说，对查询任务进度数据的一致性要求不高，只需要保证最终一致性即可，无需对整个方法加锁
     *
     * @param taskResult
     */
    public void addTaskResult(TaskResult<Result> taskResult) {
        if (TaskResultType.Success.equals(taskResult.getResultType())) {
            successCount.incrementAndGet();
        }
        proceedCount.incrementAndGet();
        taskQueue.addLast(taskResult);
        if (proceedCount.get() == jobLength) {
            // 任务执行完毕
            // 结果放到定时缓存中，到期后清除
            processor.putJob(jobName, expireTime);
        }
    }
}
