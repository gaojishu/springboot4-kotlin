package com.example.core.admin.service.impl

import cn.hutool.captcha.CaptchaUtil
import com.example.core.admin.domain.service.CaptchaDomainService
import com.example.core.admin.dto.response.CaptchaRes
import com.example.core.admin.service.CaptchaService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CaptchaServiceImpl(
    private val captchaDomainService: CaptchaDomainService
): CaptchaService {
    override fun createCaptcha(): CaptchaRes {
        val uuid = UUID.randomUUID().toString()
        // 使用 Hutool 生成验证码对象
        val captcha = CaptchaUtil.createGifCaptcha(100, 40, 4)

        // 调用领域服务进行持久化
        captchaDomainService.saveCaptcha(uuid, captcha.code)

        return CaptchaRes(uuid, captcha.imageBase64Data)
    }

    override fun validateCaptcha(code: String, uuid: String) {
        // 直接转发给领域服务
        captchaDomainService.validateAndConsume(uuid, code)
    }

}
