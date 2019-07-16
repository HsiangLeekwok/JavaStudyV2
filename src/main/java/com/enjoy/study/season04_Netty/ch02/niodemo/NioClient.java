package com.enjoy.study.season04_Netty.ch02.niodemo;

import java.io.IOException;
import java.util.Scanner;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/16 09:06<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: NIO 客户端实现<br/>
 * <b>Description</b>:
 */
public class NioClient {
    private static NioClientHandle handle;

    public static void start() {
        if (null != handle) {
            handle.stop();
        }
        handle = new NioClientHandle(Const.DEFAULT_SERVER_IP, Const.DEFAULT_PORT);
        new Thread(handle, "client").start();
    }

    public static boolean sendMessage(String msg) throws IOException {
        handle.sendMessage(msg);
        return true;
    }

    public static void main(String[] args) throws IOException {
        start();
        Scanner scanner = new Scanner(System.in);
        while (sendMessage(scanner.next())) ;
    }
}
