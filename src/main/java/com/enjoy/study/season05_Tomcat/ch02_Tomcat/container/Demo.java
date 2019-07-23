package com.enjoy.study.season05_Tomcat.ch02_Tomcat.container;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/23 16:50<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class Demo {
    public static void main(String[] args) {
        String request = "这是一个Servlet请求";
        // 创建一个管道
        StandardPipeline pipeline = new StandardPipeline();
        // 三个阀门(一个基础的，2个定制的)
        StandardValve standardValve = new StandardValve();
        FirstValve firstValve = new FirstValve();
        SecondValve secondValve = new SecondValve();
        // 设置基础阀门
        pipeline.setBasic(standardValve);
        pipeline.addValve(firstValve);
        pipeline.addValve(secondValve);
        // 调用对象管道中的第一个阀门
        pipeline.getFirst().invoke(request);
    }
}

