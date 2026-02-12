package com.example.core.admin.service

import com.example.core.admin.dto.response.admin.AdminLoginRes

interface TokenService {
    fun createToken(adminId: Long): AdminLoginRes
    fun getAdminIdByToken(token: String): Long?
}
