package com.example.core.admin.converter

import com.example.core.admin.dto.res.admin.AdminItemRes
import com.example.data.admin.entity.AdminEntity
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface AdminConverter {
    companion object {
        val INSTANCE: AdminConverter = Mappers.getMapper(AdminConverter::class.java)
    }

    fun toRes(entity: AdminEntity): AdminItemRes

}
