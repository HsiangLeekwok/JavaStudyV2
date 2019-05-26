package com.enjoy.study.season01_Concurrent.ch001;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/04/16 20:35<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Description</b>:
 */
public class _01_OnlyMain {

    public static void main(String[] args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] ids = threadMXBean.getAllThreadIds();
        for (long id : ids) {
            ThreadInfo info = threadMXBean.getThreadInfo(id);
            System.out.println("[" + id + "] " + info.getThreadName());
        }
    }
}
