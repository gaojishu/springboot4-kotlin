package com.example.core.admin.query

import com.example.core.admin.dto.req.batch.job.BatchJobQueryReq
import com.example.data.admin.enums.SortEnum
import com.example.data.generated.admin.tables.references.BATCH_JOB_EXECUTION
import com.example.data.generated.admin.tables.references.BATCH_JOB_INSTANCE
import org.jooq.Condition
import org.jooq.SortField
import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class BatchJobQueryProvider {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    fun buildCondition(params: BatchJobQueryReq.Params): Condition {

        var condition = DSL.noCondition()

        params.createdAt?.takeIf { it.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(
                    BATCH_JOB_EXECUTION.CREATE_TIME.between(
                    LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
            }
        }

        return condition
    }

    fun buildOrderBy(sort: BatchJobQueryReq.Sort?): List<SortField<*>> {
        val sortFields = mutableListOf<SortField<*>>()

        if(sort?.id == null){
            sortFields.add(BATCH_JOB_INSTANCE.JOB_INSTANCE_ID.desc())
        }else{
            sortFields.add(if (sort.id == SortEnum.ASCEND) BATCH_JOB_INSTANCE.JOB_INSTANCE_ID.asc() else BATCH_JOB_INSTANCE.JOB_INSTANCE_ID.desc())
        }

        return sortFields
    }
}