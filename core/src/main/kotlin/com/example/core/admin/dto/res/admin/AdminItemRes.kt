package com.example.core.admin.dto.res.admin

import com.example.core.admin.dto.res.BaseRes
import com.example.data.admin.enums.admin.AdminDisabledStatusEnum
import com.example.data.admin.enums.permission.PermissionTypeEnum

data class AdminItemRes(
    /** 用户名 */
    var username: String = "",

    /** 昵称 */
    var nickname: String? = null,

    /** 邮箱 */
    var email: String? = null,

    /** 手机号 */
    var mobile: String? = null,

    /**
     * 权限标识
     * 直接透传 List，前端拿到的是标准的 JSON 数组格式 ["admin:add", "user:list"]
     */
    var permissionKey: List<String>? = null,

    /** 禁用状态枚举值 */
    var disabledStatus: AdminDisabledStatusEnum? = null,

    var deletedAt: String? = null
): BaseRes()
