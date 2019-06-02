package com.enjoy.study.season02_JVM.ch04.load;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/02 21:28<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class SubClass extends SuperClass {

    static {
        System.out.println("SubClass init!");
    }
}

