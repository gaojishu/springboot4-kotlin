package com.example.data.admin.converter.admin

import com.example.data.admin.enums.admin.AdminDisabledStatusEnum

class AdminDisabledStatusConverter : org.jooq.impl.AbstractConverter<Short, AdminDisabledStatusEnum>(
    Short::class.javaObjectType,
    AdminDisabledStatusEnum::class.java
) {
    override fun from(db: Short?): AdminDisabledStatusEnum? = db?.let { AdminDisabledStatusEnum.fromValue(it) }
    override fun to(user: AdminDisabledStatusEnum?): Short? = user?.value
}

