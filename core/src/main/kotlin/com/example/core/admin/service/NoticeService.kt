package com.example.core.admin.service

import com.example.core.admin.dto.req.notice.NoticeQueryReq
import com.example.core.admin.dto.res.notice.NoticeRes
import org.springframework.data.domain.Page

/**
 * @author xkl
 * @since 2026-02-12
 */
interface NoticeService {
    fun create(
        adminId: Long?,
        title: String,
        content: String,
        attachments: List<String>?,
    )

    fun page(req: NoticeQueryReq): Page<NoticeRes>


}
