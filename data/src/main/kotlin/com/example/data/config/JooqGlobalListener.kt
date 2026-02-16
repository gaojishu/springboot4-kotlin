package com.example.data.config

import org.jooq.RecordContext
import org.jooq.RecordListener

class JooqGlobalListener : RecordListener {

    override fun insertStart(ctx: RecordContext) {
        handleEmptyString(ctx)
        handleTimestamps(ctx, isInsert = true)
    }

    override fun updateStart(ctx: RecordContext) {
        handleEmptyString(ctx)
        handleTimestamps(ctx, isInsert = false)
    }

    private fun handleEmptyString(ctx: RecordContext) {
        val record = ctx.record()
        // 遍历 Record 中的所有字段
        record.fields().forEach { field ->
            val value = record.get(field)
            // 判断：如果是 String 类型且是空字符串 ""
            if (value is String && value.isEmpty()) {
                // 强制设为 null
                record.set(field, null)
            }
        }
    }

    private fun handleTimestamps(ctx: RecordContext, isInsert: Boolean) {
        // ... 你之前维护 created_at 和 updated_at 的代码
    }
}
