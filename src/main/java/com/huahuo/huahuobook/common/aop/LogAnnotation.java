package com.huahuo.huahuobook.common.aop;

import java.lang.annotation.*;

/**
 * @作者 花火
 * @创建日期 2023/3/9 16:32
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";
    String operator() default "";
}
