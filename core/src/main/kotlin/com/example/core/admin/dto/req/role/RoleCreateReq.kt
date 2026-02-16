package com.example.core.admin.dto.req.role

data class RoleCreateReq(
    val name: String? = null,
    val remark: String? = null,
    val permissionKey: List<String>? = null
)
