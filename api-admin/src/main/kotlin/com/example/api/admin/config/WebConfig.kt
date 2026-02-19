package com.example.api.admin.config

import com.example.api.admin.middleware.interceptor.OpLogInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val opLogInterceptor: OpLogInterceptor
): WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(opLogInterceptor)
            .addPathPatterns("/**") // 拦截所有路径
    }

}