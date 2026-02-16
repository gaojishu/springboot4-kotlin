package com.example.data.admin.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.handlers.Jackson3TypeHandler
import com.example.data.admin.enums.admin.AdminDisabledStatusEnum
import java.time.LocalDateTime

@TableName("admin.admin", autoResultMap = true)
data class AdminEntity(

    /** 用户名 */
    var username: String = "",

    /** 密码 */
    var password: String = "",

    /** 昵称 */
    var nickname: String? = null,

    /** 邮箱 */
    var email: String? = null,

    /** 手机号 */
    var mobile: String? = null,

    /** 权限标识 */
    @TableField(typeHandler = Jackson3TypeHandler::class)
    var permissionKey: List<String>? = null, // 也可以是 Map<String, Any> 或自定义 DTO

    /** 禁用状态 (0: 正常, 1: 禁用) */
    var disabledStatus: AdminDisabledStatusEnum = AdminDisabledStatusEnum.DISABLED_FALSE,

    /** 逻辑删除：MyBatis-Plus 自动管理，无需手动赋值 */
    @TableLogic
    @TableField(select = false) // 查询时默认不返回该字段
    var deletedAt: LocalDateTime? = null
    ): BaseEntity()