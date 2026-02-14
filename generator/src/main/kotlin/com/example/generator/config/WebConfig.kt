package com.example.generator.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer  {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // 允许所有路径
            .allowedOriginPatterns("*") // 允许所有来源（Spring Boot 2.4+ 推荐使用）
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
            .allowedHeaders("*") // 允许所有请求头
            .allowCredentials(true) // 是否允许携带 Cookie
            .maxAge(3600) // 预检请求有效期（单位秒）
    }
}