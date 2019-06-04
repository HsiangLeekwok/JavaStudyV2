package com.enjoy.study.season02_JVM.ch05.memory_leak;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/04 20:34<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class Stack {
    public Object[] elements;
    private int size = 0;

    public Stack() {
        super();
        //elements;
    }

    public void push(Object object) {
        elements[size] = object;
        size++;
    }

    public Object pop() {
        size = size - 1;
        Object o = elements[size];
        return o;
    }
}
