package com.enjoy.study.season02_JVM.ch02;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/28 21:32<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 泛型接口<br/>
 * <b>Description</b>:
 */
public interface Generator<T> {
    T next();
}
