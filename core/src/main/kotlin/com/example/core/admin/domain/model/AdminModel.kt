package com.example.core.admin.domain.model

import com.example.base.exception.BusinessException
import com.example.data.admin.enums.admin.AdminDisabledStatusEnum
import com.example.data.generated.admin.tables.records.AdminRecord
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun AdminRecord.verifyIsDisabled() {
    if (this.disabledStatus == AdminDisabledStatusEnum.DISABLED_TRUE) {
        throw BusinessException("账号已被禁用")
    }
}

fun AdminRecord.verifyPassword(rawPassword: String,passwordEncoder: BCryptPasswordEncoder) {
    if (!passwordEncoder.matches(rawPassword, this.password)) {
        throw BusinessException("用户名或密码错误")
    }
}


