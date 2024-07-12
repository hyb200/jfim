package com.abin.chatserver.common.service;

import com.abin.chatserver.common.domain.enums.ErrorEnum;
import com.abin.chatserver.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class LockService {

    private final RedissonClient redissonClient;

    public <T> T executeWithLockThrows(String key, int waitTime, TimeUnit unit, SupplierThrow<T> supplier) throws Throwable {
        RLock lock = redissonClient.getLock(key);
        boolean success = lock.tryLock(waitTime, unit);
        if (!success) {
            throw new BusinessException(ErrorEnum.LOCK_ERROR);
        }
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    public <T> T executeWithLock(String key, int waitTime, TimeUnit unit, Supplier<T> supplier) {
        return executeWithLockThrows(key, waitTime, unit, supplier::get);
    }


    public <T> T executeWithLock(String key, TimeUnit unit, Supplier<T> supplier) {
        return executeWithLock(key, -1, TimeUnit.MILLISECONDS, supplier);
    }

    @FunctionalInterface
    public interface SupplierThrow<T> {

        T get() throws Throwable;
    }
}
