package com.enjoy.study.ch006;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/13 13:12<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class TestTjsinfo {

    String str = new String("good");
    char[] ch = {'w', 'e', 'l', 'l'};

    public static void main(String[] args) {
        TestTjsinfo ex = new TestTjsinfo();
        ex.change(ex.str, ex.ch);
        System.out.print(ex.str + " and ");
        System.out.print(ex.ch);
    }

    public void change(String str, char ch[]) {
        str = "test ok";// 栈内局部变量更改不影响外面
        ch[0] = 'g';// 传入的是对象，更改会直接反应到该对象的堆内实体内容
    }
}
