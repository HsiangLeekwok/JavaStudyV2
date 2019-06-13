package com.enjoy.study.season02_JVM.ch07.p15;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/13 21:54<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class Test {

    private static class Log {
        public static void debug(String msg) {
            if (isDebug()) {
                //System.out.println(msg);
            }
        }

        public static boolean isDebug() {
            return false;
        }
    }

    public static void main(String[] args) {
        int count = 1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Log.debug("The system is running and the time is " +
                    System.currentTimeMillis() +
                    " now, Let's do another thing: " + System.nanoTime());
        }

        System.out.println("直接打印模式，次数：" + count + "，spend time: " +
                (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            if (Log.isDebug()) {
                Log.debug("The system is running and the time is " +
                        System.currentTimeMillis() +
                        " now, Let's do another thing: " + System.nanoTime());
            }
        }
        System.out.println("先判断再打印模式，次数：" + count + "，spend time: " +
                (System.currentTimeMillis() - start) + "ms");
    }
}
