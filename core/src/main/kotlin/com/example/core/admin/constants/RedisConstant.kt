package com.example.core.admin.constants

import java.util.concurrent.TimeUnit

/**
 * 基础设施层：Redis 相关常量定义
 */
object RedisConstant {

    /**
     * 认证 Token 相关的 Key 命名空间
     */
    object Auth {
        // 基础前缀
        private const val PREFIX = "admin:auth:"

        // 登录 Token 的 Key：admin:auth:token:UUID
        // 采用函数形式动态拼接，语义化更强
        fun tokenKey(token: String) = "${PREFIX}token:$token"

        // Token 过期时间（单位：秒），例如 2 小时
        const val TOKEN_EXPIRE_SECONDS = 7200L

        val TOKEN_EXPIRE_UNIT = TimeUnit.SECONDS
    }

    object Captcha {
        private const val PREFIX = "admin:captcha:"

        // 验证码 Key: admin:captcha:uuid
        fun captchaKey(uuid: String) = "${PREFIX}$uuid"

        const val EXPIRE_SECONDS = 60L

        val EXPIRE_UNIT = TimeUnit.SECONDS
    }
}
