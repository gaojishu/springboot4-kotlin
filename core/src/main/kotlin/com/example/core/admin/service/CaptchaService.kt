package com.example.core.admin.service

import com.example.core.admin.dto.response.CaptchaRes

interface CaptchaService {
    fun createCaptcha(): CaptchaRes
    fun validateCaptcha(code: String, uuid: String)
}
