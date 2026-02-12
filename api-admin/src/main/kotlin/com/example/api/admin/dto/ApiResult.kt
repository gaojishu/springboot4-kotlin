package com.example.api.admin.dto

import java.io.Serializable

data class ApiResult<T>(
    var code: Int = 0,
    var message: String? = null,
    var data: T? = null,
    var success: Boolean = false
) : Serializable {

    // 自定义 setter 并返回对象本身
    fun code(code: Int) = apply { this.code = code }

    fun message(message: String?) = apply { this.message = message }

    fun data(data: T?) = apply {
        this.data = data
    }

    companion object {
        // 静态工厂方法作为链式起点
        fun <T> ok(): ApiResult<T> {
            return ApiResult(
                code = 0,
                success = true,
                message = null,
                data = null
            )
        }

        fun <T>error(): ApiResult<T> {
            return ApiResult(
                code = 500,
                success = false,
                message = "系统异常",
                data = null
            )
        }
    }
}
