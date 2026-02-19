package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.op.log.OpLogQueryReq
import com.example.core.admin.dto.res.op.log.OpLogItemRes
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import com.example.core.admin.service.OpLogService
import jakarta.validation.Valid
import org.springframework.data.domain.Page

@RestController
@RequestMapping("/op_log")
class OpLogController{
    @Autowired
    lateinit var opLogService: OpLogService

    @PostMapping("/page")
    fun page(@Valid @RequestBody req: OpLogQueryReq): ApiResult<Page<OpLogItemRes>> {
        val page = opLogService.page(req)
        return ApiResult.ok<Page<OpLogItemRes>>().data(page)
    }

    @PostMapping("/export")
    fun export(@Valid @RequestBody req: OpLogQueryReq): ApiResult<Long> {
        val res = opLogService.export(req)
        return ApiResult.ok<Long>().message("操作成功").data(res)
    }
}
