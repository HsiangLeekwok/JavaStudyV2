package com.enjoy.openclass._20190517.pool;

import java.sql.Connection;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/17 20:43<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 数据库池化接口<br/>
 * <b>Description</b>:
 */
public interface MySqlPool {

    // 初始化
    void init();

    // 销毁
    void destroy();

    // 取一个链接
    Connection getConnection();

    // 释放一个链接
    void release(Connection connection);
}
