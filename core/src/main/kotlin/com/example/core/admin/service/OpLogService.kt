package com.example.core.admin.service

import com.example.core.admin.dto.req.op.log.OpLogQueryReq
import com.example.core.admin.dto.res.op.log.OpLogItemRes
import org.springframework.data.domain.Page

/**
 * @author xkl
 * @since 2026-02-12
 */
interface OpLogService{
    fun page(req: OpLogQueryReq): Page<OpLogItemRes>
    fun export(req: OpLogQueryReq): Long
    fun create(
        adminId: Long?,
        ip: String?,
        method: String?,
        remark: String?,
        duration: Long,
        uri: String?,
        params: String?,
        queryParams: String?
    )
}
