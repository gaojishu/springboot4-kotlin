package com.example.core.admin.converter

import com.example.core.admin.dto.res.op.log.OpLogItemRes
import com.example.data.admin.entity.OpLogEntity
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface OpLogConverter {
    companion object {
        val INSTANCE: OpLogConverter = Mappers.getMapper(OpLogConverter::class.java)
    }

    fun toRes(entity: OpLogEntity): OpLogItemRes
}
