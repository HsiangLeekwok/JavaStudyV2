package com.enjoy.study.season01_Concurrent.ch008.edu.internal;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 20:59<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class TaskResult<Result> {
    /**
     * 执行返回结果类型
     */
    private final TaskResultType resultType;
    /**
     * 执行结果
     */
    private final Result returnValue;
    /**
     * 失败后的原因
     */
    private final String reason;

    public TaskResult(TaskResultType resultType, Result returnValue, String reason) {
        this.resultType = resultType;
        this.returnValue = returnValue;
        this.reason = reason;
    }

    public TaskResultType getResultType() {
        return resultType;
    }

    public Result getReturnValue() {
        return returnValue;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "resultType=" + resultType +
                ", returnValue=" + returnValue +
                ", reason='" + reason + '\'' +
                '}';
    }
}
