package com.enjoy.study.season02_JVM.ch05;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/04 21:43<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class JinfoTest {
    public static void main(String[] args) {
        while (true) {
            byte[] b = null;
            for (int i = 0; i < 10; i++) {
                b = new byte[1 * 1024 * 1024];
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
