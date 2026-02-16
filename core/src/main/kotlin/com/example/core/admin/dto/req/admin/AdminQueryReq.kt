package com.example.core.admin.dto.req.admin

import com.example.data.admin.enums.SortEnum
import com.example.data.admin.enums.admin.AdminDisabledStatusEnum
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.Valid
import jakarta.validation.constraints.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class AdminQueryReq(
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

        val username: String? = null,
        val mobile: String? = null,
        val email: String? = null,
        val nickname: String? = null,
        val disabledStatus: AdminDisabledStatusEnum? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Sort(
        val id: SortEnum? = null
    )
}