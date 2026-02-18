package com.example.base.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "auth.token")
data class TokenProperties(
    var expire: Long = 7200,
    var prefix: String = ""
)
