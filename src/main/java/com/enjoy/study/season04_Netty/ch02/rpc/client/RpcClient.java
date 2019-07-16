package com.enjoy.study.season04_Netty.ch02.rpc.client;

import com.enjoy.study.season04_Netty.ch02.rpc.client.frame.RpcClientFrame;
import com.enjoy.study.season04_Netty.ch02.rpc.proxy.serial.UserInfo;
import com.enjoy.study.season04_Netty.ch02.rpc.server.service.SendSms;
import com.enjoy.study.season04_Netty.ch02.rpc.server.service.StockService;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 21:38<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: Rpc客户端，调用远端的服务<br/>
 * <b>Description</b>:
 */
public class RpcClient {

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo("Leekwok", "xxx@xx.com");

        SendSms sendSms = RpcClientFrame.getRemoteProxyObject(SendSms.class, "127.0.0.1", 9189);
        System.out.println("Send mail: " + sendSms.sendMail(userInfo));

        StockService stockService = RpcClientFrame.getRemoteProxyObject(StockService.class, "127.0.0.1", 9190);
        stockService.addStock("A001", 1000);
        stockService.deduceStock("B002", 50);
    }
}
