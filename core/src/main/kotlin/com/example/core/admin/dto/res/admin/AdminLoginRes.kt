package com.example.core.admin.dto.res.admin

data class AdminLoginRes(
    val token: String,
    val expiredAt: String,
    val header: String,
    val prefix: String
)
