package com.enjoy.openclass._20190514.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/14 15:19<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@Target(ElementType.TYPE)
public @interface TestService {
    String value()default "";
}
