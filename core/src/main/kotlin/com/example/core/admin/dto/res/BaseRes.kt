package com.example.core.admin.dto.res

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

abstract class BaseRes{
    var id: Long? = null

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createdAt: LocalDateTime? = null

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var updatedAt: LocalDateTime? = null
}
