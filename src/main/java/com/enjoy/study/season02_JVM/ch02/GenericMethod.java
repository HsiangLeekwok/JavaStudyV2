package com.enjoy.study.season02_JVM.ch02;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/28 21:36<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 泛型方法<br/>
 * <b>Description</b>:
 */
public class GenericMethod {

    public <T> T genericMethod(T t) {
        return t;
    }

    public void text(int x, int y) {
        System.out.println(x + y);
    }

    public static void main(String[] args) {
        GenericMethod genericMethod = new GenericMethod();
        genericMethod.text(13, 7);
        System.out.println(genericMethod.genericMethod("Text"));
        System.out.println(genericMethod.genericMethod(180));
    }
}
