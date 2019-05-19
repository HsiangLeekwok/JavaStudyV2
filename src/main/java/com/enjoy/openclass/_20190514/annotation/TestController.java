package com.enjoy.openclass._20190514.annotation;

import java.lang.annotation.*;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/14 15:12<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */

@Target(ElementType.TYPE)// 只能在类上使用
@Retention(RetentionPolicy.RUNTIME)// 表示在运行时可以通过反射获取载体
@Documented
public @interface TestController {
    String value() default "";
}
