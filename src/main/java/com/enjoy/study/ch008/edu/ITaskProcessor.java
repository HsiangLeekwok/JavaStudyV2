package com.enjoy.study.ch008.edu;

import com.enjoy.study.ch008.edu.internal.TaskResult;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 21:00<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>: 任务执行接口，对外暴露
 */
public interface ITaskProcessor<Data, Result> {

    TaskResult<Result> execute(Data t);
}
