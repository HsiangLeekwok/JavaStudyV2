package com.enjoy.study.season02_JVM.ch01;

/**
 * <b>Author</b>: 小果<br/>
 * <b>Date</b>: 2019/05/28 09:46<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: JVM第一节课作业<br/>
 * <b>Description</b>: 运用所学知识，解释一下代码的运行结果
 */
public class ch01_HomeWork {

    public static void main(String[] args) {
        // 字符串常量池中增加一个"享学"常量(字面量)
        // 堆中新增一个本地变量b，其引用指向常量池中的"享学"地址
        String b = "享学";
        // 常量池中新增一个常量"课堂"，跟 b 所指向的常量池中字面常量("享学")合并生成新常量"享学课堂"，然后返回常量"享学课堂"的地址给新增的本地变量 a
        String a = b + "课堂";
        // a.intern(): 将变量a的内容"享学课堂"存入常量池并返回其在常量池中的地址，因常量池中已有"享学课堂"，所以直接返回其地址
        // 比较 a.intern() 和 a: a.intern() 返回的是常量池中"享学课堂"的地址，而变量 a 指向的也是这个地址，所以 print 打印出的结果是 true
        System.out.println(a.intern() == a);
    }
}
