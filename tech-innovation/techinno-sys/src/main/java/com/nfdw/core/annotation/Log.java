package com.nfdw.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**

 * 记录日志
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface Log {
    public enum LOG_TYPE {ADD, UPDATE, DEL, SELECT, ATHOR,LOGIN}

    ;

    /**
     * 内容
     */
    String desc();

    /**
     * 类型 curd
     */
    LOG_TYPE type() default LOG_TYPE.ATHOR;
}
