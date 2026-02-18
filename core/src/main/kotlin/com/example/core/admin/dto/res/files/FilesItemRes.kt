package com.example.core.admin.dto.res.files

import com.example.core.admin.dto.res.BaseRes
import com.example.core.admin.dto.res.files.category.FilesCategoryItemRes
import com.example.data.admin.enums.files.FilesTypeEnum

data class FilesItemRes(
    var name: String? = null,
    var key: String? = null,
    var mimeType: String? = null,
    var hash: String? = null,
    var remark: String? = null,
    var size: Long? = null,
    var type: FilesTypeEnum? = null,
    var categoryId: Long? = null,
    var category: FilesCategoryItemRes? = null,
): BaseRes()