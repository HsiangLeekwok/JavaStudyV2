package com.enjoy.study.season02_JVM.ch05.oom;

import java.util.LinkedList;
import java.util.List;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/04 20:15<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>: VM args: -Xmx30m -Xmx30m
 */
public class HeapOom {

    public static void main(String[] args) {

        // 1、直接抛出 OutOfMemoryError: Java heap space
//        String[] strings = new String[100 * 1024 * 1024];

        // 2、GC overhead limit exceeded
        // 需要加 JVM args: -XX:+PrintGCDetails
        List<Object> list = new LinkedList<>();
        int i = 0;
        while (true) {
            i++;
            if (i % 1000 == 0) {
                System.out.println("i = " + i);
            }
            list.add(new Object());
        }
        // 如果垃圾回收线程占用了超过 98% 的资源，但回收的效率不足 2% 的话，就会抛出此异常

//        Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
//        at com.enjoy.study.season02_JVM.ch05.oom.HeapOom.main(HeapOom.java:27)
//        Heap
//        PSYoungGen      total 9216K, used 247K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
//        eden space 8192K, 3% used [0x00000000ff600000,0x00000000ff63de58,0x00000000ffe00000)
//        from space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
//        to   space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
//        ParOldGen       total 17920K, used 793K [0x00000000fe200000, 0x00000000ff380000, 0x00000000ff600000)
//        object space 17920K, 4% used [0x00000000fe200000,0x00000000fe2c66b8,0x00000000ff380000)
//        Metaspace       used 3623K, capacity 4506K, committed 4864K, reserved 1056768K
//        class space    used 392K, capacity 394K, committed 512K, reserved 1048576K
    }
}
