package com.example.base.config

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.example.base.config.properties.OssProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OssConfig(
    private val ossProperties: OssProperties
) {

    @Bean(destroyMethod = "shutdown") // 容器销毁时自动关闭连接
    fun ossClient(): OSS {
        return OSSClientBuilder().build(
            ossProperties.endpoint,
            ossProperties.accessKey,
            ossProperties.accessSecret
        )
    }
}
