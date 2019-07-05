package com.enjoy.study.season04_Netty.ch02.rpc.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/05 23:22<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 动态代理实现接口方法<br/>
 * <b>Description</b>:
 */
public class DynamicServant implements InvocationHandler {

    private Object receptionist;

    public DynamicServant(Object recept) {
        this.receptionist=recept;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("James find recept.....");
        Object result=method.invoke(receptionist,args);
        System.out.println("half price......");
        return result;
    }
}
