package com.enjoy.study.season02_JVM.ch05.oom;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/04 20:11<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 栈溢出demo<br/>
 * <b>Description</b>: 虚拟机栈默认 1M 大小<br />
 * StackOverflowError
 * OutOfMemoryError(1M 大小，5k个线程 = 5G，如果内存小于 5G 就会抛出这个错误)
 */
public class StackOverflow {

    public void king() {
        king();// 死递归
    }

    public static void main(String[] args) {
        StackOverflow stackOverflow = new StackOverflow();
        stackOverflow.king();
    }
}
