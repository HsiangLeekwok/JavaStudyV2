package com.enjoy.study.season02_JVM.ch05;

import java.util.LinkedList;
import java.util.List;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/04 20:55<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 * -Xmx30m -Xmx30m -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 */
public class DumpOom {

    public static void main(String[] args) {
        List<Object> list = new LinkedList<>();
        int i = 0;
        while (true) {
            i++;
            if (i % 10000 == 0) System.out.println("i=" + i);
            list.add(new Object());// node 40-24=16
        }
    }
}
