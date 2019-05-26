package com.enjoy.study.season01_Concurrent.ch008.doc.entity;

import java.util.concurrent.Future;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/21 09:32<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 并发处理题目的返回结果<br/>
 * <b>Description</b>: 题目处理结果有两种，要么是题目的文本，要么是正在处理的future
 */
public class TaskResult {
    /**
     * 题目的文本
     */
    private final String detail;
    /**
     * 正在处理中的题目
     */
    private final Future<QuestionInCache> future;

    /**
     * 只有题目文本
     *
     * @param detail
     */
    public TaskResult(String detail) {
        this.detail = detail;
        this.future = null;
    }

    /**
     * 题目正在处理中
     *
     * @param future
     */
    public TaskResult(Future<QuestionInCache> future) {
        this.future = future;
        this.detail = null;
    }

    public String getDetail() {
        return detail;
    }

    public Future<QuestionInCache> getFuture() {
        return future;
    }
}
