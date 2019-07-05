package com.enjoy.study.season04_Netty.ch02.rpc.proxy;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/05 23:37<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class Receptionist implements IGetServant {
    @Override
    public void choose(String type) {
        System.out.println("Choose " + type);
    }
}
