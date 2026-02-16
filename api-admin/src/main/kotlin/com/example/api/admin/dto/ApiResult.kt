package com.example.api.admin.dto

data class ApiResult<T>(
    var code: Int = 0,
    var message: String? = null,
    var data: T? = null,
    var success: Boolean = false
) {
    // 链式 setter
    fun code(code: Int) = apply { this.code = code }
    fun message(message: String?) = apply { this.message = message }
    fun data(data: T?) = apply { this.data = data }
    fun success(success: Boolean) = apply { this.success = success }

    companion object {
        fun <T>ok(): ApiResult<T> {
            return ApiResult(
                code = 0,
                message = null,
                success = true,
                data = null
            )
        }


        fun <T>error(): ApiResult<T> {
            return ApiResult(
                code = 0,
                message = null,
                success = false,
                data = null
            )
        }

    }

}