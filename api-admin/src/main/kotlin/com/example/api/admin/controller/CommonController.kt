package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.base.dto.ValueLabel
import com.example.core.admin.dto.res.CaptchaRes
import com.example.core.admin.service.CaptchaService
import com.example.data.admin.enums.admin.AdminDisabledStatusEnum
import com.example.data.admin.enums.permission.PermissionTypeEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/common")
class CommonController{

    @Autowired private lateinit var captchaService: CaptchaService

    @GetMapping("/enums")
    fun enums(): ApiResult<Map<String, List<ValueLabel<String>>>>{
        val enum = mapOf(
            AdminDisabledStatusEnum::class.simpleName!!.replaceFirstChar { it.lowercase() }
                    to AdminDisabledStatusEnum.getAllValueLabel(),
            PermissionTypeEnum::class.simpleName!!.replaceFirstChar { it.lowercase() }
                    to PermissionTypeEnum.getAllValueLabel()

        )
        return ApiResult.ok<Map<String, List<ValueLabel<String>>>>().data(enum)
    }

    @GetMapping("/captcha")
    fun captcha(): ApiResult<CaptchaRes> {
        val captcha = captchaService.createCaptcha()

        return ApiResult.ok<CaptchaRes>().data(captcha)
    }

}
