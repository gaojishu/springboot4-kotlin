package com.example.core.admin.dto.req.files

import jakarta.validation.constraints.NotBlank

data class FilesDeleteReq(
    @field:NotBlank(message = "不能为空")
    val keys: List<String> = emptyList()
)