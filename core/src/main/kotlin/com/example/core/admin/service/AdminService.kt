package com.example.core.admin.service

import com.baomidou.mybatisplus.extension.service.IService
import com.example.core.admin.dto.response.admin.AdminLoginRes
import com.example.core.admin.dto.request.admin.AdminLoginReq
import com.example.core.admin.dto.response.admin.AdminItemRes
import com.example.data.admin.entity.AdminEntity

interface AdminService: IService<AdminEntity>  {
    fun login(adminLoginReq: AdminLoginReq): AdminLoginRes
    fun info(): AdminItemRes?
}