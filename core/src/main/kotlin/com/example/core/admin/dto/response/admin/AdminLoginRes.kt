package com.example.core.admin.dto.response.admin

data class AdminLoginRes(
    val token: String,
    val expiredAt: String,
    val header: String,
    val prefix: String
)
