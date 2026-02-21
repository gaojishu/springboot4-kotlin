package com.example.base.provider.captcha

import com.example.base.dto.CaptchaDTO

interface CaptchaProvider {
    fun createCaptcha(): CaptchaDTO
    fun validateAndConsume(uuid: String, inputCode: String): Boolean
}