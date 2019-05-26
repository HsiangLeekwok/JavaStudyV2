package com.enjoy.study.season01_Concurrent.ch008.edu;

import com.enjoy.study.season01_Concurrent.ch008.edu.internal.TaskResult;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 21:00<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>: 任务执行接口，对外暴露
 */
public interface ITaskProcessor<Data, Result> {

    /**
     * 用户自定义的业务执行代码
     * @param data
     * @return
     */
    TaskResult<Result> execute(Data data);
}
