package com.example.core.admin.dto.res.role

import com.example.core.admin.dto.res.BaseRes

data class RoleItemRes(
    var name: String? = null,
    var remark: String? = null,
    var permissionKey: List<String>? = null,
): BaseRes()
