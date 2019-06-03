package com.enjoy.study.season02_JVM.ch04.dispatch;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/02 21:17<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class DynamicDispatch {

    static abstract class Human {
        protected abstract void sayHello();
    }

    static class Man extends Human {
        @Override
        protected void sayHello() {
            System.out.println("hello, gentleman!");
        }
    }

    static class Woman extends Human {
        @Override
        protected void sayHello() {
            System.out.println("hello, lady!");
        }
    }

    public static void main(String[] args) {
        Human h1 = new Man();
        Human h2 = new Woman();
        h1.sayHello();
        h2.sayHello();

        h1 = new Woman();
        h1.sayHello();
    }
}
