package com.example.core.admin.dto.res.batch.job

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class BatchJobInstanceRes(
    var jobInstanceId: Long,
    var jobName: String,
    var jobKey: String,
    var version: Long
)

data class BatchJobExecutionRes(
    var jobExecutionId: Long,
    var jobInstanceId: Long,
    var version: Long,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createTime: LocalDateTime?,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var startTime: LocalDateTime?,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var endTime: LocalDateTime?,
    var status: String,
    var exitCode: String,
    var exitMessage: String,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var lastUpdated: LocalDateTime?,
    var jobInstanceRes: BatchJobInstanceRes?
)
