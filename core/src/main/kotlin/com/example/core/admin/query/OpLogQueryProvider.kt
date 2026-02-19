package com.example.core.admin.query

import com.example.core.admin.dto.bo.op.log.OpLogQueryBO
import com.example.data.admin.enums.SortEnum
import com.example.data.generated.admin.tables.references.OP_LOG
import org.jooq.Condition
import org.jooq.SortField
import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class OpLogQueryProvider {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    fun buildCondition(params: OpLogQueryBO.Params): Condition {

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
                    LocalDateTime.parse(startTime, formatter),
                    LocalDateTime.parse(endTime, formatter)
                ))
            }
        }

        return condition
    }

    fun buildOrderBy(sort: OpLogQueryBO.Sort?): List<SortField<*>> {
        val sortFields = mutableListOf<SortField<*>>()

        if(sort?.id == null){
            sortFields.add(OP_LOG.ID.desc())
        }else{
            sortFields.add(if (sort.id == SortEnum.ASCEND) OP_LOG.ID.asc() else OP_LOG.ID.desc())
        }

        return sortFields
    }
}