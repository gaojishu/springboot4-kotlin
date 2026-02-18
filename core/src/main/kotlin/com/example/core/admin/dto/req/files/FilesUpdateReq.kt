package com.example.core.admin.dto.req.files

data class FilesUpdateReq(
    val id: Long,
    val name: String? = null,
    val categoryId: Long? = null,
)