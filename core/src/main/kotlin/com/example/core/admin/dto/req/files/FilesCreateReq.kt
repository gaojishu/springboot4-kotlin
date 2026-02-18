package com.example.core.admin.dto.req.files

data class FilesCreateReq(
    val fileList: List<FilesItem>,
){
    data class FilesItem(
        val name: String?,
        val categoryId: Long?,
        val hash: String,
        val key: String,
        val size: Long,
        val mimeType: String,
    )
}