package com.abin.transaction.aspect;

import cn.hutool.core.date.DateUtil;
import com.abin.transaction.annotation.SecureInvoke;
import com.abin.transaction.domain.SecureInvokeDTO;
import com.abin.transaction.domain.SecureInvokeRecord;
import com.abin.transaction.service.SecureInvokeHolder;
import com.abin.transaction.service.SecureInvokeService;
import com.abin.transaction.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class SecureInvokeAspect {

    private final SecureInvokeService secureInvokeService;

    @Around("@annotation(secureInvoke)")
    public Object around(ProceedingJoinPoint joinPoint, SecureInvoke secureInvoke) throws Throwable {
        boolean async = secureInvoke.async();
        boolean inTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        if (SecureInvokeHolder.isInvoking() || !inTransaction) {
            return joinPoint.proceed();
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        List<String> parameters = Stream.of(method.getParameterTypes()).map(Class::getName).toList();
        SecureInvokeDTO secureInvokeDTO = SecureInvokeDTO.builder()
                .className(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(JsonUtils.toStr(parameters))
                .args(JsonUtils.toStr(joinPoint.getArgs()))
                .build();
        SecureInvokeRecord record = SecureInvokeRecord.builder()
                .secureInvokeDTO(secureInvokeDTO)
                .maxRetryTimes(secureInvoke.maxRetryTimes())
                .nextRetryTime(DateUtil.offsetSecond(new Date(), (int) SecureInvokeService.RETRY_INTERVAL_SECONDS))
                .build();
        secureInvokeService.invoke(record, async);
        return null;
    }
}
