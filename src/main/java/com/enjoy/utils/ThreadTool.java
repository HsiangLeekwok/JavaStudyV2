package com.enjoy.utils;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 11:59<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 线程工具<br/>
 * <b>Description</b>: 提供线程休眠方法
 */
public class ThreadTool {

    /**
     * 睡眠指定的秒数
     *
     * @param seconds 秒
     */
    public static void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 睡眠指定毫秒数
     *
     * @param milliseconds 毫秒数
     */
    public static void sleepMilliseconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
