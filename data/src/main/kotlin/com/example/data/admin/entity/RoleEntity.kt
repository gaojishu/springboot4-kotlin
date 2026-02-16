package com.example.data.admin.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.handlers.Jackson3TypeHandler

@TableName("admin.role", autoResultMap = true)
data class RoleEntity(
        var name: String? = null,
        var remark: String? = null,

        @TableField(typeHandler = Jackson3TypeHandler::class)
        var permissionKey: List<String>? = null
) : BaseEntity()
