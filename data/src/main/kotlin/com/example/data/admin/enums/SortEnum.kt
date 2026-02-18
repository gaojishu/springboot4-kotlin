package com.example.data.admin.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue


enum class SortEnum (
    val value: String
){
    ASCEND("ascend"),
    DESCEND("descend");

    companion object {
        @JvmStatic
        @JsonCreator
        fun fromValue(value: String): SortEnum? {
            return entries.find { it.value.equals(value, ignoreCase = true) }
        }
    }

    @JsonValue
    fun toValue(): String = value
}