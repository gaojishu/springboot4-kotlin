package com.example.api.admin.handler

import com.example.api.admin.dto.ApiResult
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class CustomAccessDeniedHandler(private val objectMapper: ObjectMapper) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val status = HttpStatus.FORBIDDEN
        // 1. 构建 Result 对象
        val result = ApiResult.Companion.error<Unit>()
            .message("无权限.")
            .code(status.value())

        // 2. 写入 Response
        response.status = status.value()
        response.contentType = "application/json;charset=UTF-8"
        response.writer.write(objectMapper.writeValueAsString(result))
    }
}