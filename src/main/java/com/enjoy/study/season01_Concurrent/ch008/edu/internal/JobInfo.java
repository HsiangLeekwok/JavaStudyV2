package com.enjoy.study.season01_Concurrent.ch008.edu.internal;

import com.enjoy.study.season01_Concurrent.ch008.edu.ITaskProcessor;
import com.enjoy.study.season01_Concurrent.ch008.edu.JobCheckProcessor;

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
     * 工作名称
     */
    private final String jobName;
    // 任务长度
    private final int jobLength;
    // 工作的业务处理器
    private final ITaskProcessor<?, ?> taskProcessor;

    // 任务执行成功次数
    private AtomicInteger successCount;
    // 任务已经处理的数量
    private AtomicInteger proceedCount;

    // 存放每个任务的处理结果
    private LinkedBlockingDeque<TaskResult<Result>> taskQueue;
    // 任务超时时间
    private final long expireTime;

    // 任务超时检测处理器
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

    public int getFailureCount() {
        return proceedCount.get() - successCount.get();
    }

    public int getJobLength() {
        return jobLength;
    }

    /**
     * 查询处理进度
     *
     * @return
     */
    public String getProgress() {
        return "Success[" + successCount.get() + "], Current[" + proceedCount.get() + "], Total[" + jobLength + "]";
    }

    // 提供工作中每个任务的处理结果

    /**
     * 查询每个任务的处理结果
     *
     * @return
     */
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
     * @param taskResult 任务执行完的结果
     */
    public void addTaskResult(TaskResult<Result> taskResult) {
        if (TaskResultType.Success.equals(taskResult.getResultType())) {
            // 成功的任务数量加 1
            successCount.incrementAndGet();
        }
        // 总的任务处理进度加 1
        proceedCount.incrementAndGet();
        taskQueue.addLast(taskResult);
        if (proceedCount.get() == jobLength) {
            // 任务执行完毕
            // 结果放到定时缓存中，到期后清除
            processor.putJob(jobName, expireTime);
        }
    }
}
