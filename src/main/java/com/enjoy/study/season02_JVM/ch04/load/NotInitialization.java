package com.enjoy.study.season02_JVM.ch04.load;

import java.util.concurrent.CountDownLatch;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/02 21:29<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class NotInitialization {

    public static void main(String[] args) {
        // 1、通过子类直接引用父类的静态变量 ——> 只会触发父类的初始化，不会触发子类的初始化
//        System.out.println(SubClass.value);

        // 2、使用数组的方式，会不会打印初始化
        //SuperClass[] sca = new SuperClass[10];

        // 3、打印父类的final常量，不会导致父类初始化 ——> 常量编译后直接在常量池，不会初始化类
        //System.out.println(SuperClass.HELLO_WORLD);

        // 4、如果使用常量去引用另外一个变量 ——> 会触发类初始化，只有类初始化之后才能知道变量值是多少
//        System.out.println(SuperClass.WHAT);

        CountDownLatch latch=new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(SubClass.value);
                }
            });
            thread.start();
            latch.countDown();
        }
    }
}
