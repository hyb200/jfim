package com.abin.chatserver.common.config;

import com.abin.chatserver.common.factory.CustomizedThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig implements AsyncConfigurer {
    /**
     * 项目通用线程池
     */
    public static final String JFCHAT_EXECUTOR = "jfchatExecutor";

    /**
     * WebSocket 通信线程池
     */
    public static final String WS_EXECUTOR = "websocketExecutor";

    @Override
    public Executor getAsyncExecutor() {
        return jfchatExecutor();
    }

    @Bean(WS_EXECUTOR)
    public ThreadPoolTaskExecutor websocketExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setWaitForTasksToCompleteOnShutdown(true); //  线程池优雅停机
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("websocket-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());    //  队列满了就丢弃
        executor.setThreadFactory(new CustomizedThreadFactory(executor));
        executor.initialize();
        return executor;
    }

    @Bean(JFCHAT_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor jfchatExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setWaitForTasksToCompleteOnShutdown(true); //  线程池优雅停机
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("jfchat-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());    //  队列满了就用让调用线程执行
        executor.setThreadFactory(new CustomizedThreadFactory(executor));
        executor.initialize();
        return executor;
    }
}
