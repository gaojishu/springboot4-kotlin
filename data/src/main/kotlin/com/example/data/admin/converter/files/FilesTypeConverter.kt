package com.example.data.admin.converter.files

import com.example.data.admin.enums.files.FilesTypeEnum
import org.jooq.impl.AbstractConverter

class FilesTypeConverter : AbstractConverter<String, FilesTypeEnum>(
    String::class.javaObjectType,
    FilesTypeEnum::class.java
) {
    override fun from(db: String?): FilesTypeEnum? = db?.let { FilesTypeEnum.fromValue(it) }
    override fun to(user: FilesTypeEnum?): String? = user?.value
}

