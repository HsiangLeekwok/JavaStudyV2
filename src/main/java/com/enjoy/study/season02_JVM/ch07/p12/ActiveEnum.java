package com.enjoy.study.season02_JVM.ch07.p12;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/13 22:08<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class ActiveEnum {

    public enum NormalActive {

        PLUS, MINUS, MULTI, DIVIDE;

        double oper(double x, double y) {
            switch (this) {
                case PLUS:
                    return x + y;
                case MINUS:
                    return x - y;
                case MULTI:
                    return x * y;
                case DIVIDE:
                    return x / y;
            }
            throw new NoSuchMethodException();
        }
    }

}
