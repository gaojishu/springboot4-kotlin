package com.example.core.admin.domain.model

import com.example.base.exception.BusinessException
import com.example.data.admin.entity.AdminEntity
import com.example.data.admin.enums.admin.AdminDisabledStatusEnum
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun AdminEntity.verifyIsDisabled() {
    if (this.disabledStatus == AdminDisabledStatusEnum.DISABLED_TRUE) {
        throw BusinessException("账号已被禁用")
    }
}

fun AdminEntity.verifyPassword(rawPassword: String,passwordEncoder: BCryptPasswordEncoder) {
    if (!passwordEncoder.matches(rawPassword, this.password)) {
        throw BusinessException("用户名或密码错误")
    }
}


