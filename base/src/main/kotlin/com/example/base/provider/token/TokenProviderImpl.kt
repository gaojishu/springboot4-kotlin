package com.example.base.provider.token

import com.example.base.config.properties.TokenProperties
import com.example.base.dto.TokenDTO
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.concurrent.TimeUnit

@Component
class TokenProviderImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val tokenProperties: TokenProperties
): TokenProvider {
    override fun createToken(adminId: Long): TokenDTO {
        val token = UUID.randomUUID().toString()
        val key = tokenProperties.prefix + token
        redisTemplate.opsForValue().set(
            key,
            adminId.toString(),
            tokenProperties.expire,
            TimeUnit.SECONDS
        )

        // 1. 获取当前时间（或者计算过期时间）
        val now = LocalDateTime.now()

// 2. 创建格式化器 (注意: yyyy-MM-dd HH:mm:ss 是标准格式)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

// 3. 生成字符串
        val timeStr = now.format(formatter)

        return TokenDTO(
            token = token,
            expiredAt = timeStr,
        )
    }

    override fun getIdByToken(token: String): Long? {
        val key = tokenProperties.prefix + token
        // 1. 从外部系统（Redis）获取数据
        val id = redisTemplate.opsForValue().get(key)?.toLongOrNull()
        if (id == null) {
            return null
        }
        // 3. 执行后续业务（续期）
        redisTemplate.expire(
            key,
            tokenProperties.expire,
            TimeUnit.SECONDS
        )

        return id
    }

    override fun deleteToken(token: String): Boolean {
        val key = tokenProperties.prefix + token
        val res = redisTemplate.delete(key)

        return res
    }


}
