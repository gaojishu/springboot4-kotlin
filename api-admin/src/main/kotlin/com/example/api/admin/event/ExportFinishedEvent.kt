package com.example.api.admin.event

/**
 * 导出完成事件
 * @param username 触发
 * @param attachments 上传后的文件Key/URL
 * @param rowCount 导出的行数
 * @param title 标题
 * @param content 描述
 */
data class ExportFinishedEvent(
    val username: String,
    val attachments: List<String>,
    val rowCount: Long,
    val title: String,
    val content: String
)