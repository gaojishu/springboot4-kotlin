package com.example.core.admin.service.impl

import com.example.core.admin.dto.req.batch.job.BatchJobQueryReq
import com.example.core.admin.dto.res.batch.job.BatchJobExecutionRes
import com.example.core.admin.dto.res.batch.job.BatchJobInstanceRes
import com.example.core.admin.query.BatchJobQueryProvider
import com.example.core.admin.service.BatchJobService
import com.example.data.extension.paginate
import com.example.data.generated.admin.tables.references.BATCH_JOB_EXECUTION
import com.example.data.generated.admin.tables.references.BATCH_JOB_INSTANCE
import org.jooq.DSLContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class BatchJobServiceImpl(
    private val dsl: DSLContext,
    private val queryProvider: BatchJobQueryProvider
): BatchJobService {
    override fun page(req: BatchJobQueryReq): Page<BatchJobExecutionRes> {
        val pageable = PageRequest.of(req.params.current - 1, req.params.pageSize)

        val res = dsl.paginate(
            countTable = BATCH_JOB_EXECUTION,
            condition = queryProvider.buildCondition(req.params),
            pageable = pageable,
        ) {
                context ->
            context.select(
                *BATCH_JOB_EXECUTION.fields(),
                *BATCH_JOB_INSTANCE.fields()
            )
                .from(BATCH_JOB_EXECUTION)
                .leftJoin(BATCH_JOB_INSTANCE).on(BATCH_JOB_EXECUTION.JOB_INSTANCE_ID.eq(BATCH_JOB_INSTANCE.JOB_INSTANCE_ID))
                .orderBy(queryProvider.buildOrderBy(req.sort))
        }

        return res.map {
                record->
            val execution = record.into(BATCH_JOB_EXECUTION).into(BatchJobExecutionRes::class.java)
            val instance = record.into(BATCH_JOB_INSTANCE).into(BatchJobInstanceRes::class.java)

            execution.apply {
                jobInstanceRes = instance
            }

        }
    }
}