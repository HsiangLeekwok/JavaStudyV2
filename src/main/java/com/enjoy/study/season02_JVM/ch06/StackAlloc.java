package com.enjoy.study.season02_JVM.ch06;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/11 20:56<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 逃逸分析<br/>
 * <b>Description</b>: 栈上分配
 */
public class StackAlloc {

    public static class User {
        public int id = 0;
        public String name = "";
    }

    public static void alloc() {
        User u = new User();// object 在堆上分配的，有逃逸分析的技术则在栈中分配
        u.id = 5;
        u.name = "King";
    }

    public static void main(String[] args) {
        long b = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            alloc();
        }
        long e = System.currentTimeMillis();
        System.out.println(e - b);
    }
}
