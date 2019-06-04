package com.enjoy.study.season02_JVM.ch05.memory_leak;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/04 20:35<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class UseStackDemo {

    public static void main(String[] args) {
        Stack stack = new Stack();
        Object o = new Object();
        System.out.println("o=" + o);
        stack.push(o);

        Object o1 = stack.pop();
        System.out.println("o1=" + o1);
    }
}
