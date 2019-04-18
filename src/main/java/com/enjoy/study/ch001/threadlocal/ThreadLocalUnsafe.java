package com.enjoy.study.ch001.threadlocal;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/04/18 21:38<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Description</b>:
 */
public class ThreadLocalUnsafe {

    private static Number number = new Number(0);



    private static class Number {
        private int number;

        public Number(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
