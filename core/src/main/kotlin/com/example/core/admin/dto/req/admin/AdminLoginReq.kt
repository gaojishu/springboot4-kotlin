package com.example.core.admin.dto.req.admin

import jakarta.validation.constraints.NotBlank

data class AdminLoginReq(
    @field:NotBlank(message = "账号不能为空")
    val username: String?,

    @field:NotBlank(message = "密码不能为空")
    val password: String?,

    @field:NotBlank(message = "验证码不能为空")
    val captchaCode: String?,

    @field:NotBlank(message = "验证码 UUID 不能为空")
    val captchaUuid: String?
)