package com.example.core.admin.dto.req.files

import com.example.data.admin.enums.SortEnum
import com.example.data.admin.enums.files.FilesTypeEnum
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.Valid
import jakarta.validation.constraints.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class FilesQueryReq(
    @field:Valid
    val params: Params,

    @field:Valid
    val sort: Sort? = null
){
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Params(
        @field:NotNull(message = "当前页码不能为空")
        @field:Min(value = 1, message = "页码必须大于等于1")
        val current: Int = 1,

        @field:NotNull(message = "每页大小不能为空")
        @field:Min(value = 1, message = "每页大小必须大于等于1")
        @field:Max(value = 100, message = "每页大小不能大于100")
        val pageSize: Int = 10,
        val name: String? = null,
        val categoryId: Long? = null,
        val type: FilesTypeEnum? = null,
        val createdAt: List<String>? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Sort(
        val id: SortEnum? = null
    )
}