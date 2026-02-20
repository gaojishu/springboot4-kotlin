package com.example.api.admin.handler

import com.example.api.admin.dto.ApiResult
import com.example.base.exception.BusinessException
import com.example.base.utils.log
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.security.access.AccessDeniedException

@RestControllerAdvice
class GlobalExceptionHandler {

    // 1. 新增这个方法，让权限异常“穿透”回到 Security 过滤器
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException) {
        throw e
    }

    /**
     * 捕获自定义业务异常 (来自 core 或 domain)
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ApiResult<Unit>> {
        log.warn("业务异常: {}", e.message)
        val result = ApiResult.error<Unit>().code(HttpStatus.BAD_REQUEST.value()).message(e.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result)
    }

    /**
     * 捕获 Spring Validation 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResult<Unit>> {
        val message = e.bindingResult.fieldError?.defaultMessage ?: "参数校验失败"
        log.warn("参数校验异常: {}", message)
        val status = HttpStatus.UNPROCESSABLE_CONTENT
        val result = ApiResult.error<Unit>().code(status.value()).message(message)
        return ResponseEntity.status(status).body(result)
    }

    /**
     * 捕获系统未知异常
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResult<Unit>> {
        log.error("系统运行异常", e)
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val result = ApiResult.error<Unit>()
            .code(status.value())
            .message(e.message ?: "系统运行异常")
        return ResponseEntity.status(status).body(result)
    }
}
