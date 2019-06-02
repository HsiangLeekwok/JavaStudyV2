package com.enjoy.study.season02_JVM.ch04.xor;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/02 22:09<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 自定义类加载器测试用例<br/>
 * <b>Description</b>:
 */
public class Demo {

    public static void main(String[] args) throws Exception {

        CustomClassLoader loader = new CustomClassLoader("My ClassLoader");
        loader.setBasePath("D:\\JavaStudy\\StudyV2.0\\target\\classes");
        Class<?> clazz = loader.findClass("com.enjoy.study.season02_JVM.ch04.xor.DemoUser");
        System.out.println(clazz.getClassLoader());
        Object object = clazz.newInstance();
        System.out.println(object);
    }
}
