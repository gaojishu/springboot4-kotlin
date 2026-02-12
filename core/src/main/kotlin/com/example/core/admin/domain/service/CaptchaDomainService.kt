package com.example.core.admin.domain.service

import com.example.base.exception.BusinessException
import com.example.core.admin.constants.RedisConstant
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class CaptchaDomainService(
    private val redisTemplate: StringRedisTemplate
) {
    /**
     * 保存验证码到缓存
     */
    fun saveCaptcha(uuid: String, code: String) {
        redisTemplate.opsForValue().set(
            RedisConstant.Captcha.captchaKey(uuid),
            code,
            RedisConstant.Captcha.EXPIRE_SECONDS,
            RedisConstant.Captcha.EXPIRE_UNIT
        )
    }

    /**
     * 校验并删除
     */
    fun validateAndConsume(uuid: String, inputCode: String) {
        val key = RedisConstant.Captcha.captchaKey(uuid)
        val redisCode = redisTemplate.opsForValue().get(key)
            ?: throw BusinessException("验证码已过期")

        if (!redisCode.equals(inputCode, ignoreCase = true)) {
            // 注意：某些业务场景下，输入错误也建议立即删除或增加错误计数
            throw BusinessException("验证码错误")
        }

        // 校验通过，阅后即焚
        redisTemplate.delete(key)
    }
}
