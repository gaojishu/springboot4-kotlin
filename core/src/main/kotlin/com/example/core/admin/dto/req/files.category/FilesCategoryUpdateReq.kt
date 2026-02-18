package com.example.core.admin.dto.req.files.category


data class FilesCategoryUpdateReq(
    val id: Long,
    val name: String? = null,
    val remark: String? = null,
)

