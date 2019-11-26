package com.danger.common.log.access;

import java.lang.annotation.*;

/**
 * 系统日志自定义注解
 *
 * @author liuyanzhao
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})//作用于参数或方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLog {
    /**
     * 日志名称
     *
     * @return
     */
    String title() default "";
    /**
     * 日志类型
     *
     * @return
     */
    String module() default "";
    String action() default "";
}