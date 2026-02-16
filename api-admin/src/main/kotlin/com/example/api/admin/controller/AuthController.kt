package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.admin.AdminLoginReq
import com.example.core.admin.dto.res.admin.AdminItemRes
import com.example.core.admin.dto.res.admin.AdminLoginRes
import com.example.core.admin.dto.res.permission.PermissionItemRes
import com.example.core.admin.security.LoginAdmin
import com.example.core.admin.service.AdminService
import com.example.core.admin.service.PermissionService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController{
    @Autowired
    private lateinit var adminService: AdminService

    @Autowired
    private lateinit var permissionService: PermissionService

    @GetMapping("/info")
    fun info(authentication: Authentication): ApiResult<AdminItemRes?> {
        val adminId = (authentication.principal as LoginAdmin).id
        val admin = adminService.selectById(adminId)
        return ApiResult.ok<AdminItemRes?>().data(admin)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody req: AdminLoginReq): ApiResult<AdminLoginRes> {
        val token = adminService.login(req)

        return ApiResult.ok<AdminLoginRes>()
            .message("登录成功")
            .data(token)
    }

    @GetMapping("/logout")
    fun logout() {
        adminService.logout()
    }

    @GetMapping("/permission")
    fun permission(authentication: Authentication): ApiResult<List<PermissionItemRes>> {
        val adminId = (authentication.principal as LoginAdmin).id
        val res = permissionService.selectByAdminId(adminId)
         return ApiResult.ok<List<PermissionItemRes>>().data(res)
    }



}
