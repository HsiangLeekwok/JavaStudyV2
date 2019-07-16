package com.enjoy.study.season04_Netty.ch02.rpc.proxy.refrection;

import java.lang.reflect.Method;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 21:18<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 演示反射的使用<br/>
 * <b>Description</b>:
 */
public class ReflectionDemo {
    public static void main(String[] args) throws Exception {

        // 实例化对象的标准用法，也就是所谓的正
        Servant servant = new Servant();
        servant.service("Hello");

        // 通过全限定名拿到类的 class 对象
        Class servantClass = Class.forName("com.enjoy.study.season04_Netty.ch02.rpc.proxy.refrection.Servant");
        Servant inst = (Servant) servantClass.newInstance();
        Method[] methods = servantClass.getMethods();
        for (Method method : methods) {
            // 打印方法名
            System.out.println(method.getName());
            if (method.getName().equals("toString")) {
                System.out.println("执行：" + method.invoke(servantClass.newInstance(), null));
            }
        }
    }
}
