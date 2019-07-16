package com.enjoy.study.season04_Netty.ch02.rpc.register;

import java.io.Serializable;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 23:51<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 提供服务的服务器节点<br/>
 * <b>Description</b>:
 */
public class ServicePoint implements Serializable {

    private final String host;
    private final int port;


    public ServicePoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServicePoint) {
            if (obj == this) return true;
            ServicePoint that = (ServicePoint) obj;
            return that.getHost().equals(host) && that.getPort() == port;
        } else {
            return false;
        }
    }
}
