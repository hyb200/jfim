package com.abin.chatserver.common.aspect;

import cn.hutool.core.util.StrUtil;
import com.abin.chatserver.common.annotation.RedissonLock;
import com.abin.chatserver.common.service.LockService;
import com.abin.chatserver.common.utils.SpElUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Order(0)
@Slf4j
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final LockService lockService;

    @Around("@annotation(com.abin.chatserver.common.annotation.RedissonLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        String prefix = StrUtil.isBlank(redissonLock.prefixKey()) ? getMethodKey(method) : redissonLock.prefixKey();
        String key = SpElUtils.parseSpEl(method, joinPoint.getArgs(), redissonLock.key());
        return lockService.executeWithLockThrows(String.format("%s:%s", prefix, key), redissonLock.waitTime(), redissonLock.unit(), joinPoint::proceed);
    }

    private String getMethodKey(Method method) {
        return method.getDeclaringClass() + "#" + method.getName();
    }
}
