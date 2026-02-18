package com.example.base.provider.captcha

import cn.hutool.captcha.CaptchaUtil
import com.example.base.dto.CaptchaDTO
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.text.equals

@Component
class CaptchaProviderImpl(
    private val redisTemplate: RedisTemplate<String, String>,

): CaptchaProvider {
    private val prefix = "admin:captcha:"
    override fun createCaptcha(): CaptchaDTO {
        val uuid = UUID.randomUUID().toString()
        // 使用 Hutool 生成验证码对象
        val captcha = CaptchaUtil.createGifCaptcha(100, 40, 4)

        redisTemplate.opsForValue().set(
            prefix+uuid,
            captcha.code,
            60L,
            TimeUnit.SECONDS
        )

        return CaptchaDTO(uuid, captcha.imageBase64Data)
    }

    /**
     * 校验并删除
     */
    override fun validateAndConsume(uuid: String, inputCode: String): Boolean {
        val key = prefix+uuid
        val redisCode = redisTemplate.opsForValue().get(key)

        if (redisCode == null || !redisCode.equals(inputCode, ignoreCase = true)) {
            return false
        }

        // 校验通过，阅后即焚
        redisTemplate.delete(key)
        return true
    }

}
