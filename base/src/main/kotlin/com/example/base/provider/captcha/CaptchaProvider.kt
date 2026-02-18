package com.example.base.provider.captcha

import com.example.base.dto.CaptchaDTO

interface CaptchaProvider {
    fun createCaptcha(): CaptchaDTO
    fun validateAndConsume(code: String, uuid: String): Boolean
}