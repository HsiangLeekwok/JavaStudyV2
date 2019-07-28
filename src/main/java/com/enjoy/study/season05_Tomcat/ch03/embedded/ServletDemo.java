package com.enjoy.study.season05_Tomcat.ch03.embedded;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/28 17:29<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 使用嵌入式启动方式启动一个 Servlet <br/>
 * <b>Description</b>:
 */
public class ServletDemo {
    public static void main(String[] args) throws Exception {

        // 自定义的 servlet ，专门处理 http 请求
        HttpServlet httpServlet = new HttpServlet() {
            @Override
            public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
                res.getWriter().write("hello world.");
            }
        };

        // 引入嵌入式 tomcat
        Tomcat tomcat = new Tomcat();
        // 部署应用的 context
        Context context = tomcat.addContext("/demo", null);
        // 往应用中添加 servlet
        Wrapper wrapper = tomcat.addServlet(context, "/simpleHttpServlet", httpServlet);
        // 添加 ServletMapping 映射信息
        context.addServletMappingDecoded("/hello", "/simpleHttpServlet");
        // 启动 tomcat, -- 生命周期
        tomcat.init();// 初始化
        tomcat.start();// 启动
        tomcat.getServer().await();
    }
}
