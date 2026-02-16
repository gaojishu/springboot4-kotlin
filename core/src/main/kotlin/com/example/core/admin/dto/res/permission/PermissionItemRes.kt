package com.example.core.admin.dto.res.permission

import com.example.core.admin.dto.res.BaseRes
import com.example.data.admin.enums.permission.PermissionTypeEnum

data class PermissionItemRes(
    var icon: String? = null,
    var level: Int? = null,
    var name: String? = null,
    var parentId: Long? = null,
    var path: String? = null,
    var remark: String? = null,
    var sort: Int? = null,
    var type: PermissionTypeEnum? = null,
    var code: String? = null,
    var key: String? = null,
): BaseRes()
