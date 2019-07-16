package com.enjoy.study.season04_Netty.ch02.rpc.server.service.impl;

import com.enjoy.study.season04_Netty.ch02.rpc.proxy.serial.UserInfo;
import com.enjoy.study.season04_Netty.ch02.rpc.server.service.SendSms;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 22:25<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class SendSmsImpl implements SendSms {
    @Override
    public boolean sendMail(UserInfo user) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("SMS has sent to " + user.getName() + "[" + user.getPhone() + "]");
        return true;
    }
}
