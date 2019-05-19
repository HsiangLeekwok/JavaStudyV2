package com.enjoy.study.ch007;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 20:31<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:在域上运用延迟初始化占位类模式
 */
public class LazyInstance {

    private Integer value;
    private static Integer heavy;// 成员变量，很耗资源

    public LazyInstance(Integer value) {
        super();
        this.value = value;
    }

    private static class InstanceHolder {
        // 域延迟加载
        static Integer val = 100;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getHeavy() {
        return InstanceHolder.val;
    }
}
