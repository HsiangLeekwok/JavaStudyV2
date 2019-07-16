package com.enjoy.study.season04_Netty.ch02.rpc.proxy.serial;

import java.io.Serializable;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 21:02<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class UserInfo implements Serializable {
    private String name;
    private String phone;

    public UserInfo(String name, String info) {
        this.name = name;
        this.phone = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
