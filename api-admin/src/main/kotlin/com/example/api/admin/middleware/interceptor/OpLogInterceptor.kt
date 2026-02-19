package com.example.api.admin.middleware.interceptor

import com.example.base.utils.log
import com.example.core.admin.security.LoginAdmin
import com.example.core.admin.service.OpLogService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.util.ContentCachingRequestWrapper

@Component
class OpLogInterceptor: HandlerInterceptor {

    @Autowired private lateinit var opLogService: OpLogService

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // 请求处理前的逻辑
        log.info("HandlerInterceptor preHandle request: ${request.requestURI}")
        // 记录请求开始时间
        val startTime = System.currentTimeMillis()
        request.setAttribute("requestStartTime", startTime)

        return true // 继续处理请求
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        // 记录请求处理时间
        val startTime = request.getAttribute("requestStartTime") as Long
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // 尝试获取被包装的请求
        val cachedRequest = request as? ContentCachingRequestWrapper
        val bodyContent = cachedRequest?.contentAsByteArray
        val requestBody: String? = bodyContent?.let { String(it, Charsets.UTF_8) }
        val queryString = request.queryString
        val authentication = SecurityContextHolder.getContext().authentication
        val admin = authentication?.principal as LoginAdmin

        opLogService.create(
            adminId = admin.id,
            duration = duration,
            ip = request.remoteAddr,
            method = request.method,
            remark = null,
            uri = request.requestURI,
            params = requestBody,
            queryParams = queryString
        )
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        // 视图渲染后的逻辑
        log.info("HandlerInterceptor afterCompletion for request: ${request.requestURI}")
    }
}