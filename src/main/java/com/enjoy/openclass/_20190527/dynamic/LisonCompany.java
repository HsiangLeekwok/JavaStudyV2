package com.enjoy.openclass._20190527.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/27 20:53<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class LisonCompany implements InvocationHandler {

    // 被代理的对象
    private Object factory;

    public Object getFactory() {
        return factory;
    }

    public void setFactory(Object factory) {
        this.factory = factory;
    }

    public Object getProxyInstance() {
        return Proxy.newProxyInstance(factory.getClass().getClassLoader(),
                factory.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        doSomethingBefore();
        Object ret = method.invoke(factory, args);
        doSomethingEnd();
        return ret;
    }

    private void doSomethingBefore() {
        System.out.println("市场调研准备工作");
    }

    private void doSomethingEnd() {
        System.out.println("精美包装，快递一条龙服务！");
    }
}
