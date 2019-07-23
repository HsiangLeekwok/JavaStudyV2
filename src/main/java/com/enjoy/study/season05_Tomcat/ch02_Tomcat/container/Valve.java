package com.enjoy.study.season05_Tomcat.ch02_Tomcat.container;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/23 15:33<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 管道阀门接口类<br/>
 * <b>Description</b>:
 */
public interface Valve {

    /**
     * 获取下一个阀门
     * @return
     */
    Valve getNext();

    /**
     * 设置下一个阀门
     * @param valve
     */
    void setNext(Valve valve);

    /**
     * 本阀门的处理过程
     * @param request
     */
    void invoke(String request);
}
