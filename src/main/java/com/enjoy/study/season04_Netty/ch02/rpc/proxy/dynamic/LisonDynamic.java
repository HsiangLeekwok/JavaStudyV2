package com.enjoy.study.season04_Netty.ch02.rpc.proxy.dynamic;

import com.enjoy.study.season04_Netty.ch02.rpc.proxy.IGetServant;
import com.enjoy.study.season04_Netty.ch02.rpc.proxy.Receptionist;

import java.lang.reflect.Proxy;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/05 23:26<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class LisonDynamic {

    public static void main(String[] args) {
        IGetServant james = (IGetServant) Proxy.newProxyInstance(IGetServant.class.getClassLoader(),
                new Class[]{IGetServant.class},
                new DynamicServant(new Receptionist()));
        james.choose("Haha");
    }
}
