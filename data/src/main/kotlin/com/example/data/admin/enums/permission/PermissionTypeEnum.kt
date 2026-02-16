package com.example.data.admin.enums.permission

import com.baomidou.mybatisplus.annotation.EnumValue
import com.example.base.dto.ValueLabel

enum class PermissionTypeEnum(
    @EnumValue val value: Int, // 告诉 MyBatis-Plus：存这个值到数据库
    val label: String // @JsonValue告诉 Jackson：返回 JSON 时显示这个文字
){
    MENU_PERMISSION(1, "菜单权限"),
    OPERATION_PERMISSION(2, "操作权限");

    companion object {
        private val map = entries.associateBy { it.value }

        fun fromValue(value: Int): PermissionTypeEnum {
            return map[value] ?: MENU_PERMISSION
        }

        fun getAllValueLabel(): List<ValueLabel<String>> {
            return entries.map {
                ValueLabel(it.name, it.label)
            }
        }

    }

}