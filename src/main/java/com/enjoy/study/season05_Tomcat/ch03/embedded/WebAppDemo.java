package com.enjoy.study.season05_Tomcat.ch03.embedded;

import org.apache.catalina.startup.Tomcat;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/28 19:22<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 要用潜入启动方式启动一个 SpringMVC 的项目<br/>
 * <b>Description</b>:
 */
public class WebAppDemo {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.addWebapp("/ref", "D:\\JavaStudy\\demo\\ref");
        tomcat.init();
        tomcat.start();
        tomcat.getServer().await();
    }
}
