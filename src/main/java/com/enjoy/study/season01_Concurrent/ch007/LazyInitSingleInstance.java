package com.enjoy.study.season01_Concurrent.ch007;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 20:25<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/> 懒汉式-双重检查
 * <b>Description</b>: 延迟初始化占位类模式
 */
public class LazyInitSingleInstance {

    private LazyInitSingleInstance() {

    }

    public static LazyInitSingleInstance getInstance() {
        return InstanceHolder.initSingleInstance;
    }

    private static class InstanceHolder {
        // 虚拟机类加载机制保证多个线程在访问时会加锁初始化
        private static LazyInitSingleInstance initSingleInstance = new LazyInitSingleInstance();
    }

}
