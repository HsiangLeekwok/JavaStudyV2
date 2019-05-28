package com.enjoy.study.season02_JVM.ch02;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/28 20:50<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>: -Xms20m -Xmx20m -Xmn10m
 * -XX:+PrintGCDetails // 打印垃圾回收日志，同时在程序退出时输出当前内存分配情况
 * -XX:PretenureSizeThreshold=4m // 超过这个值即可进行老年代分配，否则要判断Eden空闲值是否符合大小
 * -XX:+UseSerialGC
 */
public class BigAllocation {
    // 大对象直接进入老年代
    private static final int _1MB = 1024 * 1024;// 1M 大小

    public static void main(String[] args) {
        byte[] b1, b2, b3, b4;
        Object object;
        b1 = new byte[_1MB];
        b2 = new byte[_1MB];
        b3 = new byte[_1MB];
        b4 = new byte[5*_1MB];
    }
}
