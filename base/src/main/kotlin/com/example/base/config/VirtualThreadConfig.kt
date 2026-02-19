package com.example.base.config

import org.springframework.boot.task.SimpleAsyncTaskExecutorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class VirtualThreadConfig : AsyncConfigurer {

    /**
     * 1. 虚拟线程执行器 - 用于 @Async 方法
     */
    @Bean(name = ["virtualTaskExecutor"])
    override fun getAsyncExecutor(): Executor {
        return SimpleAsyncTaskExecutorBuilder()
            .threadNamePrefix("virtual-async-")
            .virtualThreads(true)
            .build()
    }

    /**
     * 2. 虚拟线程执行器 - 用于 Batch Step 的 taskExecutor
     */
    @Bean(name = ["batchTaskExecutor"])
    fun batchTaskExecutor(): TaskExecutor {
        return SimpleAsyncTaskExecutorBuilder()
            .threadNamePrefix("batch-vt-")
            .virtualThreads(true)
            .build()
    }
}