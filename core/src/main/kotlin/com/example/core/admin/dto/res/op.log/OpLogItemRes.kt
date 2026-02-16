package com.example.core.admin.dto.res.op.log

import com.example.core.admin.dto.res.BaseRes
import com.example.core.admin.dto.res.admin.AdminItemRes

data class OpLogItemRes(
    var adminId: Long? = null,
    var duration: Long? = null,
    var ip: String? = null,
    var method: String? = null,
    var remark: String? = null,
    var uri: String? = null,
    var params: String? = null,
    var queryParams: String? = null,
    var admin: AdminItemRes? = null
): BaseRes()
