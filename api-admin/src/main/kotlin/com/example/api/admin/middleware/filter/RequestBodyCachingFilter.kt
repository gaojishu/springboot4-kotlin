package com.example.api.admin.middleware.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.IOException

@Component // 确保这个 Filter 被 Spring 扫描到
@Order(1) // 可选：指定过滤器的顺序
class RequestBodyCachingFilter : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val contentType = request.contentType
        if (contentType != null && contentType.contains("application/json")) {
            val wrappedRequest = ContentCachingRequestWrapper(request, 1024 * 1024 * 10)
            filterChain.doFilter(wrappedRequest, response)
        } else {
            filterChain.doFilter(request, response)
        }
    }
}