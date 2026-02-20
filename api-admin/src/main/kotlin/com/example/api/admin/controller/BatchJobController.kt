package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.batch.job.BatchJobQueryReq
import com.example.core.admin.dto.res.batch.job.BatchJobExecutionRes
import com.example.core.admin.service.BatchJobService
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/batch_job")
class BatchJobController{
    @Autowired
    lateinit var batchJobService: BatchJobService

    @PreAuthorize("hasAuthority('batch_job:read')")
    @PostMapping("/page")
    fun page(@Valid @RequestBody req: BatchJobQueryReq): ApiResult<Page<BatchJobExecutionRes>> {
        val page = batchJobService.page(req)
        return ApiResult.ok<Page<BatchJobExecutionRes>>().data(page)
    }
}
