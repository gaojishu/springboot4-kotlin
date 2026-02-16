package com.example.core.admin.service.impl

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.core.admin.converter.OpLogConverter
import com.example.core.admin.dto.req.op.log.OpLogQueryReq
import org.springframework.stereotype.Service
import com.example.data.admin.mapper.OpLogMapper
import com.example.data.admin.entity.OpLogEntity
import com.example.core.admin.service.OpLogService
import com.example.core.admin.dto.res.op.log.OpLogItemRes
import com.example.data.admin.entity.AdminEntity
import com.github.yulichang.wrapper.MPJLambdaWrapper

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class OpLogServiceImpl: ServiceImpl<OpLogMapper, OpLogEntity>(), OpLogService {

    private fun buildWrapper(req: OpLogQueryReq) {

    }

    override fun page(req: OpLogQueryReq) {
    }

}
