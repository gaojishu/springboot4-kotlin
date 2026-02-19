package com.example.api.admin.batch

import org.jooq.Cursor
import org.jooq.Record
import org.jooq.Select
import org.springframework.batch.infrastructure.item.ExecutionContext
import org.springframework.batch.infrastructure.item.ItemStreamReader

/**
 * 自定义 jOOQ 流式 Reader
 * T: 数据库 Record 类型
 * R: 转换后的 DTO 类型
 */
open class JooqStreamingItemReader<T : Record, R: Any>(
    private val query: Select<T>,
    private val mapper: (T) -> R
) : ItemStreamReader<R> {

    private var cursor: Cursor<T>? = null
    private var iterator: Iterator<T>? = null

    override fun open(executionContext: ExecutionContext) {
        // 关键：使用 fetchLazy 开启服务端游标，fetchSize 防止 OOM
        cursor = query.fetchLazy()
        iterator = cursor?.iterator()
    }

    override fun read(): R? {
        return if (iterator?.hasNext() == true) {
            iterator?.next()?.let { mapper(it) }
        } else {
            null // 返回 null 表示数据读取完毕
        }
    }

    override fun close() {
        cursor?.close() // 必须关闭游标释放数据库资源
    }

    override fun update(executionContext: ExecutionContext) {
        // 如果需要断点续传可在此实现，通常导出任务不需要
    }
}