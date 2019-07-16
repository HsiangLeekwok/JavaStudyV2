package com.enjoy.study.season04_Netty.ch02.rpc.server.service;

import com.enjoy.study.season04_Netty.ch02.rpc.proxy.serial.UserInfo;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 22:22<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 短信息发送接口<br/>
 * <b>Description</b>:
 */
public interface SendSms {
    boolean sendMail(UserInfo user);
}
