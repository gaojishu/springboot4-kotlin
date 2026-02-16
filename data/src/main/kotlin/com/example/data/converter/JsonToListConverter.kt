package com.example.data.converter

import org.jooq.JSON
import org.jooq.impl.AbstractConverter
import tools.jackson.core.type.TypeReference
import tools.jackson.module.kotlin.jacksonObjectMapper

@Suppress("UNCHECKED_CAST")
class JsonToListConverter : AbstractConverter<JSON, List<String>>(
    JSON::class.java,
    java.util.List::class.java as Class<List<String>>
) {
    private val mapper = jacksonObjectMapper()

    override fun from(databaseObject: JSON?): List<String>? {
        // 使用 .data() 获取原始 JSON 字符串
        val jsonStr = databaseObject?.data()
        return if (jsonStr.isNullOrEmpty()) {
            null
        } else {
            mapper.readValue(jsonStr, object : TypeReference<List<String>>() {})
        }
    }

    override fun to(userObject: List<String>?): JSON? {
        return userObject?.let { JSON.json(mapper.writeValueAsString(it)) }
    }
}
