package com.example.data.admin.entity

import com.baomidou.mybatisplus.annotation.TableName
import com.example.data.admin.enums.permission.PermissionTypeEnum

@TableName("admin.permission")
data class PermissionEntity(
        var icon: String? = null,
        var level: Int? = null,
        var name: String? = null,
        var parentId: Long? = null,
        var path: String? = null,
        var remark: String? = null,
        var sort: Int? = null,
        var type: PermissionTypeEnum? = null,
        var code: String? = null,
        var key: String? = null
): BaseEntity()
