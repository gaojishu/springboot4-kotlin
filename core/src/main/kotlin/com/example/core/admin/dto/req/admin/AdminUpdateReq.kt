package com.example.core.admin.dto.req.admin

import com.example.data.admin.enums.admin.AdminDisabledStatusEnum
import jakarta.validation.constraints.NotBlank

data class AdminUpdateReq(
    @field:NotBlank(message = "id不能为空")
    val id: Long,

    val username: String?,

    val password: String?,

    val disabledStatus: AdminDisabledStatusEnum?,

    val mobile: String?,

    val email: String?,

    val nickname: String?,

    val permissionKey: List<String>?,
)