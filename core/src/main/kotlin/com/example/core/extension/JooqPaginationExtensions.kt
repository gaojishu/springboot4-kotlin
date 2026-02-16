package com.example.core.extension

import org.jooq.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

/**
 * 模拟 JPA 风格的分页
 * @param countTable 用于 count(*) 的主表
 * @param pageable Spring Data 的分页对象
 * @param condition 过滤条件
 */
fun DSLContext.paginate(
    countTable: Table<*>,
    pageable: Pageable,
    condition: Condition = org.jooq.impl.DSL.noCondition(),
    querySupplier: (DSLContext) -> SelectLimitStep<*>
): Page<Record> {
    val total = this.fetchCount(countTable, condition).toLong()

    if (total == 0L) return PageImpl(emptyList(), pageable, total)

    // 这里直接使用 .fetch()，不进行 into(clazz) 转换
    val content = querySupplier(this)
        .limit(pageable.pageSize)
        .offset(pageable.offset)
        .fetch() // 返回 Result<Record>

    return PageImpl(content, pageable, total)
}

