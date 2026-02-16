package com.example.core.admin.dto.req.permission

import com.example.data.admin.enums.permission.PermissionTypeEnum

data class PermissionUpdateReq(
    val id: Long,
    val icon: String? = null,
    val name: String? = null,
    val parentId: Long? = null,
    val path: String? = null,
    val remark: String? = null,
    val sort: Int? = null,
    val type: PermissionTypeEnum? = null,
    val code: String? = null,
)

