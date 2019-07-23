package com.enjoy.study.season05_Tomcat.ch02_Tomcat.container;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/23 16:41<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 第二个阀门实现类<br/>
 * <b>Description</b>:
 */
public class SecondValve implements Valve {
    protected Valve next = null;

    @Override
    public Valve getNext() {
        return next;
    }

    @Override
    public void setNext(Valve valve) {
        this.next = valve;
    }

    @Override
    public void invoke(String request) {
        System.out.println("第二个定制阀门处理");
        request = (request + "ooxx2");
        // 向后传递请求
        getNext().invoke(request);
    }
}
