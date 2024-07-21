package com.abin.chatserver.common.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义异常处理器，捕获异常
 */
@Slf4j
public class CustomizedUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread {} ", t.getName(), e);
    }
}
