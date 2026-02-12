package com.example.core.admin.converter

import com.example.core.admin.dto.response.admin.AdminItemRes
import com.example.data.admin.entity.AdminEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Mapper
interface AdminConverter {
    companion object {
        val INSTANCE: AdminConverter = Mappers.getMapper(AdminConverter::class.java)

        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }
    fun mapTime(value: OffsetDateTime?): String? {
        return value?.format(formatter)
    }
    @Mapping(target = "disabledStatus", source = "disabledStatus.value")
   fun toRes(entity: AdminEntity): AdminItemRes

    // 集合转换
    fun toResList(list: List<AdminEntity>): List<AdminItemRes>
}
