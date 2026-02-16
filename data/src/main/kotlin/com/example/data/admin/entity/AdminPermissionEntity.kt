package com.example.data.admin.entity

import com.baomidou.mybatisplus.annotation.TableName

@TableName("admin.admin_permission")
data class AdminPermissionEntity(
        var adminId: Long? = null,
        var permissionId: Long? = null
)
