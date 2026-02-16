package com.example.core.admin.service

import com.example.data.admin.entity.OpLogEntity
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.core.admin.dto.req.op.log.OpLogQueryReq
import com.example.core.admin.dto.res.op.log.OpLogItemRes
import com.baomidou.mybatisplus.core.metadata.IPage

/**
 * @author xkl
 * @since 2026-02-12
 */
interface OpLogService : IService<OpLogEntity>{
    fun page(req: OpLogQueryReq)

}
