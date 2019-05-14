package com.enjoy.openclass._201905141400.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/14 15:17<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */

@Target(ElementType.FIELD)// 只能在类的属性上使用
public @interface TestAutowired {
    String value() default "";
}
