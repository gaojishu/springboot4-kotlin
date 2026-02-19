package com.example.base.provider.storage

import com.aliyun.oss.OSS
import com.example.base.config.properties.OssProperties
import com.example.base.dto.storage.AliyunUploadPolicyDTO
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import tools.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.Date
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@ConditionalOnProperty(
    name = ["storage.type"],
    havingValue = "aliyun",
    matchIfMissing = true
)
@Component
class AliyunStorageProviderImpl(
    private val ossClient: OSS,
    private val ossProperties: OssProperties
): StorageProvider {
    private val host = "https://${ossProperties.bucketName}.${ossProperties.endpoint}" // 构建 host
    private val expireSeconds = 7200L // 策略有效期：7200秒 = 2小时
    private val mapper = jacksonObjectMapper()

    override fun upload(file: File): String {
        val key = rename(objectName = file.name, fileType = file.extension)
        ossClient.putObject(ossProperties.bucketName,key,file)
        return key
    }

    override fun delete(path: String) {
        ossClient.deleteObject(ossProperties.bucketName, path)
    }

    override fun exists(path: String): Boolean {
        return true
    }

    override fun copy(from: String, to: String) {
        ossClient.copyObject(ossProperties.bucketName, from, ossProperties.bucketName, to)
    }

    override fun generatePresignedUrl(path: String): String {
        // 设置预签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
        val expiration = Date.from(Instant.now().plusSeconds(3600))
        val url = ossClient.generatePresignedUrl(ossProperties.bucketName, path, expiration)
        return url.toString()
    }

    override fun move(from: String, to: String) {
        ossClient.renameObject(
            ossProperties.bucketName,
            from,
            to
        )
    }

    override fun rename(objectName: String, fileType: String): String {
        val extension = File(objectName).extension

        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        val uuid = UUID.randomUUID().toString()

        return "upload/$fileType/$currentDate/$uuid.$extension"
    }

    override fun ossUploadPolicy(): AliyunUploadPolicyDTO {

        // 1. 计算过期时间（ISO 8601 格式）
        val expiration = Instant.now().plusSeconds(expireSeconds).toString() // Kotlin 默认输出 ISO-8601

        // 2. 构建 conditions
        val conditions = mutableListOf<List<Any>>()

        // 条件1：文件大小限制 content-length-range
        conditions.add(listOf("content-length-range", 0, 1024 * 1024 * 1024)) // 0 ~ 1GB

        // 条件2：key 必须以 dir 开头
        conditions.add(listOf("starts-with", "\$key", ossProperties.uploadPrefix))

        // 可选：添加其他条件，如 content-type 等

        // 3. 构建 policy 文档
        val policyJson = mapOf(
            "expiration" to expiration,
            "conditions" to conditions
        )

        // 4. 将 policy 转为 JSON 并 Base64 编码
        val policyBytes = mapper.writeValueAsBytes(policyJson)
        val encodedPolicy = Base64.getEncoder().encodeToString(policyBytes)

        // 5. 使用 HMAC-SHA1 签名
        val secretKeySpec = SecretKeySpec(ossProperties.accessSecret.toByteArray(StandardCharsets.UTF_8), "HmacSHA1")
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(secretKeySpec)
        val signatureBytes = mac.doFinal(encodedPolicy.toByteArray(StandardCharsets.UTF_8))
        val signature = Base64.getEncoder().encodeToString(signatureBytes)

        // 6. 返回响应
        return AliyunUploadPolicyDTO(
            accessId = ossProperties.accessKey,
            host = host,
            policy = encodedPolicy,
            signature = signature,
            expire = expiration,
            dir = ossProperties.uploadPrefix
        )
    }
}