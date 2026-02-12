package com.example.base.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 定义一个通用的扩展属性 log
 * 这样任何类都可以直接通过 log.info(...) 调用
 */
val <reified T : Any> T.log: Logger
    inline get() = LoggerFactory.getLogger(T::class.java)