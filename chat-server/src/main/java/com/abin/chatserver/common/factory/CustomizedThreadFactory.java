package com.abin.chatserver.common.factory;

import com.abin.chatserver.common.handler.CustomizedUncaughtExceptionHandler;
import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;

/**
 * 装饰器模式（组合），增强线程工厂
 */
@AllArgsConstructor
public class CustomizedThreadFactory implements ThreadFactory {

    private static final Thread.UncaughtExceptionHandler EXCEPTION_HANDLER = new CustomizedUncaughtExceptionHandler();

    private ThreadFactory threadFactory;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = threadFactory.newThread(r); //  原先的线程工厂
        thread.setUncaughtExceptionHandler(EXCEPTION_HANDLER);
        return thread;
    }
}
