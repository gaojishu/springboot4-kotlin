package com.example.core.admin.service.impl

import com.example.base.exception.BusinessException
import com.example.base.provider.captcha.CaptchaProvider
import com.example.core.admin.dto.res.CaptchaRes
import com.example.core.admin.service.CaptchaService
import org.springframework.stereotype.Service

@Service
class CaptchaServiceImpl(
    private val captchaProvider: CaptchaProvider
): CaptchaService {
    override fun createCaptcha(): CaptchaRes {

        val res = captchaProvider.createCaptcha()

        return CaptchaRes(res.uuid, res.img)
    }

    override fun validateCaptcha(code: String, uuid: String) {
        val res = captchaProvider.validateAndConsume(uuid, code)
        if (!res) throw BusinessException("验证码错误")
    }

}
