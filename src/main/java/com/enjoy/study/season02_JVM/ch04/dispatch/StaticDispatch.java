package com.enjoy.study.season02_JVM.ch04.dispatch;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/02 21:14<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class StaticDispatch {

    static abstract class Human {
    }

    static class Man extends Human {
    }

    static class Woman extends Human {
    }

    public void sayHello(Human guy) {
        System.out.println("hello, guy!");
    }

    public void sayHello(Man guy) {
        System.out.println("hello, gentleman!");
    }

    public void sayHello(Woman guy) {
        System.out.println("hello, laydy!");
    }

    public static void main(String[] args) {
        Human h1 = new Man();
        Human h2 = new Woman();
        StaticDispatch sr = new StaticDispatch();
        sr.sayHello(h1);
        sr.sayHello(h2);

        Human man = new Man();
        sr.sayHello((Man) man);
        man = new Woman();
        sr.sayHello((Woman) man);
    }
}
