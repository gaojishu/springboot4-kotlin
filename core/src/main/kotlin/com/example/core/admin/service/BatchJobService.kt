package com.example.core.admin.service

import com.example.core.admin.dto.req.batch.job.BatchJobQueryReq
import com.example.core.admin.dto.res.batch.job.BatchJobExecutionRes
import org.springframework.data.domain.Page

interface BatchJobService {
    fun page(req: BatchJobQueryReq): Page<BatchJobExecutionRes>
}