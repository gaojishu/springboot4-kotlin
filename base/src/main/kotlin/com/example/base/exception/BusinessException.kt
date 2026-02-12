package com.example.base.exception

/**
 * 统一业务异常
 * @param code 错误码，默认 500
 * @param message 错误描述
 */
class BusinessException(
    val code: Int = 500,
    override val message: String
) : RuntimeException(message) {
    constructor(message: String) : this(500, message)
}
