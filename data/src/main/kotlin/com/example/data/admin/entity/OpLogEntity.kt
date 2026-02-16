package com.example.data.admin.entity

import com.baomidou.mybatisplus.annotation.TableName
import com.example.data.admin.entity.BaseEntity
import java.time.LocalDateTime

@TableName("admin.op_log")
data class OpLogEntity(
        var adminId: Long? = null,
        var duration: Long? = null,
        var ip: String? = null,
        var method: String? = null,
        var remark: String? = null,
        var uri: String? = null,
        var params: String? = null,
        var queryParams: String? = null
) : BaseEntity()
