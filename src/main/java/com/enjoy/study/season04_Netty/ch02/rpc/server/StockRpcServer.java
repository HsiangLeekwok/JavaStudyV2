package com.enjoy.study.season04_Netty.ch02.rpc.server;

import com.enjoy.study.season04_Netty.ch02.rpc.server.frame.RpcServerFrame;
import com.enjoy.study.season04_Netty.ch02.rpc.server.service.StockService;
import com.enjoy.study.season04_Netty.ch02.rpc.server.service.impl.StockServiceImpl;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 21:56<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: rpc服务端，提供服务<br/>
 * <b>Description</b>:
 */
public class StockRpcServer {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                RpcServerFrame serviceServer = new RpcServerFrame(9190);
                // 注册服务
                serviceServer.registerService(StockService.class.getName(), StockServiceImpl.class);
                serviceServer.startService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
