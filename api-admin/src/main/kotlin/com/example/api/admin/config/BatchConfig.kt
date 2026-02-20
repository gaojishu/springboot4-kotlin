package com.example.api.admin.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
@EnableJdbcJobRepository(tablePrefix = "admin.batch_")
class BatchConfig{
    
}