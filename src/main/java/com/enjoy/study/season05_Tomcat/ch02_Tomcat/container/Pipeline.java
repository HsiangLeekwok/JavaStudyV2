package com.enjoy.study.season05_Tomcat.ch02_Tomcat.container;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/23 15:33<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 管道模式：管道接口<br/>
 * <b>Description</b>:
 */
public interface Pipeline {
    /**
     * 获取第一个阀门
     *
     * @return
     */
    Valve getFirst();

    Valve getBasic();

    /**
     * 设置基础阀门
     *
     * @param valve
     */
    void setBasic(Valve valve);

    /**
     * 添加阀门
     *
     * @param valve
     */
    void addValve(Valve valve);
}
