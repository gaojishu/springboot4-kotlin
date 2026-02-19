package com.example.api.admin.batch

import org.springframework.batch.core.launch.support.JobOperatorFactoryBean
import org.springframework.batch.core.repository.JobRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor

@Configuration
class JobOperatorConfig {


    /**
     * 配置 JobOperatorFactoryBean，使用虚拟线程
     */
    @Bean
    fun asyncJobOperator(
        jobRepository: JobRepository,
        @Qualifier("batchTaskExecutor") taskExecutor: TaskExecutor
    ): JobOperatorFactoryBean {
        return JobOperatorFactoryBean().apply {
            setJobRepository(jobRepository)
            // 使用虚拟线程执行器
            setTaskExecutor(taskExecutor)
        }
    }
}


