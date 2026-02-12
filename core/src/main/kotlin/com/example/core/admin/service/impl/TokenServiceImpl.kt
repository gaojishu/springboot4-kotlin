package com.example.core.admin.service.impl

import com.example.core.admin.constants.RedisConstant
import com.example.core.admin.domain.service.TokenDomainService
import com.example.core.admin.dto.response.admin.AdminLoginRes
import com.example.core.admin.service.TokenService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class TokenServiceImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val tokenDomainService: TokenDomainService
): TokenService {
    override fun createToken(adminId: Long): AdminLoginRes {
        val token = UUID.randomUUID().toString()
        redisTemplate.opsForValue().set(
            RedisConstant.Auth.tokenKey(token),
            adminId.toString(),
            RedisConstant.Auth.TOKEN_EXPIRE_SECONDS,
            RedisConstant.Auth.TOKEN_EXPIRE_UNIT
        )

        // 1. 获取当前时间（或者计算过期时间）
        val now = LocalDateTime.now()

// 2. 创建格式化器 (注意: yyyy-MM-dd HH:mm:ss 是标准格式)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

// 3. 生成字符串
        val timeStr = now.format(formatter)

        return AdminLoginRes(
            token = token,
            expiredAt = timeStr,
            header = "Authorization",
            prefix = "Bearer",
        )
    }

    override fun getAdminIdByToken(token: String): Long? {

        // 1. 从外部系统（Redis）获取数据
        val adminId = redisTemplate.opsForValue().get(RedisConstant.Auth.tokenKey(token))?.toLongOrNull()

        // 2. 调用领域服务进行“合法性”校验
        tokenDomainService.validateToken(adminId, token)

        // 3. 执行后续业务（续期）
        redisTemplate.expire(
            RedisConstant.Auth.tokenKey(token),
            RedisConstant.Auth.TOKEN_EXPIRE_SECONDS,
            RedisConstant.Auth.TOKEN_EXPIRE_UNIT
        )

        return adminId!!
    }
}
