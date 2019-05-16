package com.enjoy.study.ch007;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 20:28<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>饿汉式
 * <b>Description</b>: 饿汉式初始化单例
 */
public class HungrySingleInstance {

    private HungrySingleInstance() {

    }

    private static HungrySingleInstance instance = new HungrySingleInstance();
}
