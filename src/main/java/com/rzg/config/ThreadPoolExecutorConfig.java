package com.rzg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolExecutorConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(
                2, // 核心线程数
                4, // 最大线程数
                100, // 空闲线程存活时间
                TimeUnit.SECONDS, // 空闲线程存活时间
                new ArrayBlockingQueue<>(4), // 任务队列
                Executors.defaultThreadFactory() ,
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
