package com.example.core.admin.dto.bo.op.log

import com.example.data.admin.enums.SortEnum

data class OpLogQueryBO(
    var params: Params? = null,
    var sort: Sort? = null
){
    data class Params(
        var current: Int = 1,
        var pageSize: Int = 10,
        var remark: String? = null,
        var method: String? = null,
        var uri: String? = null,
        var ip: String? = null,
        var adminId: Long? = null,
        var createdAt: List<String>? = null,
        var updatedAt: List<String>? = null,
    )
    data class Sort(
        var id: SortEnum? = null
    )
}
