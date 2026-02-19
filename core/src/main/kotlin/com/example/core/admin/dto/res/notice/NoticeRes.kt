package com.example.core.admin.dto.res.notice

import com.example.core.admin.dto.res.BaseRes

data class NoticeRes(
    var title: String? = null,
    var content: String? = null,
    var attachments: List<String>? = null,
    var adminId: Long? = null,
): BaseRes()
