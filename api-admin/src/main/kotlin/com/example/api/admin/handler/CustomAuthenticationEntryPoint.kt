package com.example.api.admin.handler

import com.example.api.admin.dto.ApiResult
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class CustomAuthenticationEntryPoint(private val objectMapper: ObjectMapper): AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {

        val status = HttpStatus.UNAUTHORIZED
        // 1. 构建 Result 对象
        val result = ApiResult.Companion.error<Unit>()
            .message("未登录.")
            .code(status.value())

        // 2. 写入 Response
        response.status = status.value()
        response.contentType = "application/json;charset=UTF-8"
        response.writer.write(objectMapper.writeValueAsString(result))
    }
}