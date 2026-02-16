package com.example.data.admin.converter.permission

import com.example.data.admin.enums.permission.PermissionTypeEnum
import org.jooq.impl.AbstractConverter

class PermissionTypeConverter : AbstractConverter<Short, PermissionTypeEnum>(
    Short::class.javaObjectType,
    PermissionTypeEnum::class.java
) {
    override fun from(db: Short?): PermissionTypeEnum? = db?.let { PermissionTypeEnum.fromValue(it) }
    override fun to(user: PermissionTypeEnum?): Short? = user?.value
}

