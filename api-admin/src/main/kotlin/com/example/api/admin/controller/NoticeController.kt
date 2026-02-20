package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.notice.NoticeQueryReq
import com.example.core.admin.dto.res.notice.NoticeRes
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import com.example.core.admin.service.NoticeService
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/notice")
class NoticeController{
    @Autowired
    lateinit var noticeService: NoticeService

    @PostMapping("/page")
    fun page(@RequestBody req: NoticeQueryReq): ApiResult<Page<NoticeRes>> {
        val res = noticeService.page(req)

        return ApiResult.ok<Page<NoticeRes>>().data(res)
    }

}
