package com.enjoy.study.season02_JVM.ch02;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/28 21:31<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 泛型<br/>
 * <b>Description</b>:
 */
public class NormalGeneric<T> {

    private T data;

    public NormalGeneric() {
    }

    public NormalGeneric(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static void main(String[] args) {
        NormalGeneric<String> normalGeneric = new NormalGeneric<>();
        normalGeneric.setData("Test");
        System.out.println(normalGeneric.getData());
    }
}
