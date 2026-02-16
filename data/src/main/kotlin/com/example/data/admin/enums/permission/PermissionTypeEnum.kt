package com.example.data.admin.enums.permission

import com.example.base.dto.ValueLabel

enum class PermissionTypeEnum(
    val value: Short,
    val label: String
){
    MENU_PERMISSION(1, "菜单权限"),
    OPERATION_PERMISSION(2, "操作权限");

    companion object {
        private val map = entries.associateBy { it.value }

        fun fromValue(value: Short): PermissionTypeEnum {
            return map[value] ?: MENU_PERMISSION
        }

        fun getAllValueLabel(): List<ValueLabel<String>> {
            return entries.map {
                ValueLabel(it.name, it.label)
            }
        }

    }

}