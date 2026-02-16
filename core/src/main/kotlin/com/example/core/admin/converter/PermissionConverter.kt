package com.example.core.admin.converter

import com.example.core.admin.dto.res.permission.PermissionItemRes
import com.example.data.admin.entity.PermissionEntity
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface PermissionConverter {
    companion object {
        val INSTANCE: PermissionConverter = Mappers.getMapper(PermissionConverter::class.java)
    }

    fun toRes(entity: PermissionEntity): PermissionItemRes
}
