package com.enjoy.study.season02_JVM.ch05.memory_leak;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/04 20:41<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class NoStaticInternal {

    public int k = 13;
    private static String string = "king";
    protected float j = 1.5f;

    public static void show() {
        System.out.println("show");
    }

    private void add() {
        System.out.println("add");
    }

    public static void main(String[] args) {
        NoStaticInternal no = new NoStaticInternal();
        Child c = no.new Child();
        c.test();
    }

    // 内部类 child ———— 静态的，防止内存泄漏
    class Child {
        public int i;

        public void test() {
            System.out.println("k=" + k);
            System.out.println("string:" + string);
            add();
            System.out.println("j=" + j);
            show();
        }
    }

}
