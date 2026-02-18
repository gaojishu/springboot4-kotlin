package com.example.data.admin.enums.files

import com.example.base.dto.ValueLabel

enum class FilesTypeEnum(val value: String, val label: String) {
    IMAGE("image", "图片"),
    VIDEO("video", "视频"),
    AUDIO("audio", "音频"),
    OTHER("other", "其它");

    companion object {
        private val map = entries.associateBy { it.value }

        fun fromValue(value: String): FilesTypeEnum {
            return map[value] ?: OTHER
        }

        fun getAllValueLabel(): List<ValueLabel<String>> {
            return entries.map {
                ValueLabel(it.name, it.label)
            }
        }
    }
}