package com.abin.chatserver.common.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisSerializeConfig.class)
public @interface EnableRedisSerialize {
}
