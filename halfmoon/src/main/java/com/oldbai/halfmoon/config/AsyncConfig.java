package com.oldbai.halfmoon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 开启异步操作
 *
 * @author 老白
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心数
        executor.setCorePoolSize(2);
        //最大线程池
        executor.setMaxPoolSize(10);
        //线程名称
        executor.setThreadNamePrefix("bai-hui-hui-email");
        //容量，队列容量
        executor.setQueueCapacity(30);
        //初始化
        executor.initialize();
        return executor;
    }
}
