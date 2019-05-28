package com.enjoy.study.season02_JVM.ch02;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/28 21:40<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class NeedGeneric {

    public int addInt(int x, int y) {
        return x + y;
    }

    public float addFloat(float x, float y) {
        return x + y;
    }

    public static void main(String[] args) {
        NeedGeneric needGeneric = new NeedGeneric();
        System.out.println(needGeneric.addInt(1, 2));
        System.out.println(needGeneric.addFloat(1.2f, 2.4f));

        // 使用泛型
        System.out.println(needGeneric.add(1, 2));
        System.out.println(needGeneric.add(1.1, 2.2));
    }

    public <T extends Number> double add(T x, T y) {
        return x.doubleValue() + y.doubleValue();
    }
}
