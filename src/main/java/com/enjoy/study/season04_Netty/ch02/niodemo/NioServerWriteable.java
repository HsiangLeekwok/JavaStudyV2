package com.enjoy.study.season04_Netty.ch02.niodemo;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/16 09:07<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: NIO 服务端实现<br/>
 * <b>Description</b>:
 */
public class NioServerWriteable {
    private static NioServerHandlerWriteable handler;

    public static void start() {
        if (null != handler) {
            handler.stop();
        }
        handler = new NioServerHandlerWriteable(Const.DEFAULT_PORT);
        new Thread(handler, "server").start();
    }

    public static void main(String[] args) {
        start();
    }
}
