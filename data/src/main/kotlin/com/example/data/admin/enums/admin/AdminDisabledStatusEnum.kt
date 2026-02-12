package com.example.data.admin.enums.admin

import com.baomidou.mybatisplus.annotation.EnumValue
import com.example.base.dto.ValueLabel

enum class AdminDisabledStatusEnum(
    @EnumValue val value: Short, // 告诉 MyBatis-Plus：存这个值到数据库
    val label: String // @JsonValue告诉 Jackson：返回 JSON 时显示这个文字
){
    DISABLED_TRUE(1, "禁用"),
    DISABLED_FALSE(0, "启用");

    companion object {
        private val map = entries.associateBy { it.value }

        fun fromValue(value: Short): AdminDisabledStatusEnum {
            return map[value] ?: DISABLED_FALSE
        }

        fun getAllValueLabel(): List<ValueLabel<Short>> {
            return entries.map {
                ValueLabel(it.value, it.label)
            }
        }

    }

}