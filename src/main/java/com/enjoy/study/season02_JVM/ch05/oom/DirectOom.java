package com.enjoy.study.season02_JVM.ch05.oom;

import java.nio.ByteBuffer;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/04 20:28<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>: -XX:MaxDirectMemorySize=100m
 */
public class DirectOom {

    public static void main(String[] args) {
        // java.lang.OutOfMemoryError: Direct buffer memory
        ByteBuffer buffer=ByteBuffer.allocateDirect(128*1024*1024);
    }
}
