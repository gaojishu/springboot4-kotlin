package com.example.core.admin.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@MapperScan("com.example.data.admin.mapper")
@Configuration
class MybatisPlusConfig {
    @Bean
    fun mybatisPlusInterceptor():MybatisPlusInterceptor{
        val interceptor = MybatisPlusInterceptor ()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.POSTGRE_SQL))
        return interceptor
    }
}