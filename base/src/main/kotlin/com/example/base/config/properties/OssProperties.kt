package com.example.base.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
class OssProperties {
    var endpoint: String = ""
    var accessKey: String = ""
    var accessSecret: String = ""
    var bucketName: String = ""
    var uploadPrefix: String = ""
}
