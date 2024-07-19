package com.abin.transaction.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 保证方法成功执行。如果在事务内的方法，会将操作记录入库。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecureInvoke {

    int maxRetryTimes() default 3;

    /**
     * 默认异步执行，先入库，后续通过定时任务异步执行
     * @return  是否异步执行
     */
    boolean async() default true;
}
