package com.example.data.admin.enums.admin

import com.example.base.dto.ValueLabel

enum class AdminDisabledStatusEnum(
    val value: Short,
    val label: String
){
    DISABLED_TRUE(1, "禁用"),
    DISABLED_FALSE(0, "启用");

    companion object {
        private val map = entries.associateBy { it.value }

        fun fromValue(value: Short): AdminDisabledStatusEnum {
            return map[value] ?: DISABLED_FALSE
        }

        fun getAllValueLabel(): List<ValueLabel<String>> {
            return entries.map {
                ValueLabel(it.name, it.label)
            }
        }

    }

}