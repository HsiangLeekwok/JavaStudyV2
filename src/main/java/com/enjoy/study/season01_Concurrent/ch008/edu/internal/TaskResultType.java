package com.enjoy.study.season01_Concurrent.ch008.edu.internal;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 20:57<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>任务执行结果
 * <b>Description</b>:
 */
public enum TaskResultType {
    /**
     * 执行成功
     */
    Success,
    /**
     * 执行失败
     */
    Failure,
    /**
     * 执行发生了异常
     */
    Exception
}
