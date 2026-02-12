package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.request.admin.AdminLoginReq
import com.example.core.admin.dto.response.admin.AdminItemRes
import com.example.core.admin.dto.response.admin.AdminLoginRes
import com.example.core.admin.service.AdminService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AdminController{
    @Autowired
    private lateinit var adminService: AdminService

    @GetMapping("/index")
    fun index(): ApiResult<String> {
        val thread = Thread.currentThread()
        // 打印线程详细信息到控制台
        println("当前线程: $thread")
        println("是否为虚拟线程: ${thread.isVirtual}")

        val info = "Name: ${thread.name}, IsVirtual: ${thread.isVirtual}"
        return ApiResult.ok<String>().data(info)
    }

    @GetMapping("/info")
    fun info(): ApiResult<AdminItemRes?> {
        val admin = adminService.info()
        return ApiResult.ok<AdminItemRes?>().data(admin)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody req: AdminLoginReq): ApiResult<AdminLoginRes> {
        val token = adminService.login(req)

        return ApiResult.ok<AdminLoginRes>()
            .message("登录成功")
            .data(token)
    }

}
