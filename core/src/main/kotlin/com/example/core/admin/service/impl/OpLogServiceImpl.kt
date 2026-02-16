package com.example.core.admin.service.impl

import com.example.core.admin.dto.req.op.log.OpLogQueryReq
import org.springframework.stereotype.Service
import com.example.core.admin.service.OpLogService
import com.example.core.admin.dto.res.op.log.OpLogItemRes
import com.example.core.extension.paginate
import com.example.data.admin.enums.SortEnum
import com.example.data.generated.admin.tables.references.OP_LOG
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.SortField
import org.jooq.impl.DSL
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class OpLogServiceImpl(
    private val dsl: DSLContext
): OpLogService {

    private fun buildCondition(params: OpLogQueryReq.Params): Condition {

        var condition = DSL.noCondition()

        params.adminId?.let {
            condition = condition.and(OP_LOG.ADMIN_ID.eq(it))
        }
        params.method?.let {
            condition = condition.and(OP_LOG.METHOD.contains(it))
        }
        params.uri?.let {
            condition = condition.and(OP_LOG.URI.contains(it))
        }
        params.createdAt?.takeIf { it.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(OP_LOG.CREATED_AT.between(
                    LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
            }
        }
        params.updatedAt?.takeIf { it.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(OP_LOG.UPDATED_AT.between(
                    LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
            }
        }

        return condition
    }

    private fun buildOrderBy(sort: OpLogQueryReq.Sort?): List<SortField<*>> {
        val sortFields = mutableListOf<SortField<*>>()

        // 1. 处理 ID 排序
        sort?.id?.let { order ->
            val field = OP_LOG.ID
            sortFields.add(if (order == SortEnum.ASCEND) field.asc() else field.desc())
        }

        return sortFields
    }

    override fun page(req: OpLogQueryReq): Page<OpLogItemRes> {
        val pageable = PageRequest.of(req.params.current - 1, req.params.pageSize)

        val condition = buildCondition(req.params)
        val orderBy = buildOrderBy(req.sort)
        val res = dsl.paginate(
            countTable = OP_LOG,
            condition = condition,
            pageable = pageable,
        ){
                context ->
            context.select(OP_LOG)
                .from(OP_LOG)
                .where(condition)
                .orderBy(orderBy)
        }

        return res.map { record ->
            record.into(OpLogItemRes::class.java)
        }
    }

}
