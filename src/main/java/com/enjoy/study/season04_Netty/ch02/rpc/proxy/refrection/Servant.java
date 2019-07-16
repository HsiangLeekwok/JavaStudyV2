package com.enjoy.study.season04_Netty.ch02.rpc.proxy.refrection;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 21:16<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class Servant {

    public boolean service(String message) {
        System.out.println(message + " is good....");
        return true;
    }

    @Override
    public String toString() {
        return "I an No.14";
    }
}
