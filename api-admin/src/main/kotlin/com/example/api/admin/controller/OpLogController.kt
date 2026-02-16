package com.example.api.admin.controller

import com.baomidou.mybatisplus.core.metadata.IPage
import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.op.log.OpLogQueryReq
import com.example.core.admin.dto.res.op.log.OpLogItemRes
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import com.example.core.admin.service.OpLogService
import jakarta.validation.Valid

@RestController
@RequestMapping("/op_log")
class OpLogController{
    @Autowired
    lateinit var opLogService: OpLogService

    @PostMapping("/page")
    fun page(@Valid @RequestBody req: OpLogQueryReq){
        val page = opLogService.page(req)
//        return ApiResult.ok<IPage<OpLogItemRes>>().data(page)
    }
}
