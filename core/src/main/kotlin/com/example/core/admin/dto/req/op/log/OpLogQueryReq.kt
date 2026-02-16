package com.example.core.admin.dto.req.op.log

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpLogQueryReq(
    @field:Valid
    val params: Params,

    @field:Valid
    val sort: Sort? = null
){
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Params(
        @field:NotNull(message = "当前页码不能为空")
        @field:Min(value = 1, message = "页码必须大于等于1")
        val current: Long = 1,

        @field:NotNull(message = "每页大小不能为空")
        @field:Min(value = 1, message = "每页大小必须大于等于1")
        @field:Max(value = 100, message = "每页大小不能大于100")
        val pageSize: Long = 10,

        val remark: String? = null,
        val method: String? = null,
        val uri: String? = null,
        val ip: String? = null,
        val adminId: Long? = null,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime? = null,
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Sort(
        val id: String? = null
    )
}