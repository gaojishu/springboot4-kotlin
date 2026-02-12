package com.example.core.admin.domain.service

import com.example.base.exception.BusinessException
import org.springframework.stereotype.Component

@Component
class TokenDomainService {
    fun validateToken(adminId: Long?, token: String?) {
        if (adminId == null || token == null) {
            throw BusinessException("请先登录")
        }
    }
}