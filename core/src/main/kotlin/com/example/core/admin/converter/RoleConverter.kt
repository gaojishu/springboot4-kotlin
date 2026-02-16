package com.example.core.admin.converter

import com.example.core.admin.dto.res.role.RoleItemRes
import com.example.data.admin.entity.RoleEntity
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface RoleConverter {
    companion object {
        val INSTANCE: RoleConverter = Mappers.getMapper(RoleConverter::class.java)
    }

    fun toRes(entity: RoleEntity): RoleItemRes
}
