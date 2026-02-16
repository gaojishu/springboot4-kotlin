package com.example.core.admin.service.impl

import com.example.base.exception.BusinessException
import com.example.core.admin.security.LoginAdmin
import com.example.data.generated.admin.tables.references.ADMIN_
import org.jooq.DSLContext
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AdminUserDetailsServiceImpl(
    private val dsl: DSLContext
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(id: String): UserDetails {
        val id = id.toLong()
        val record = dsl.fetchOne(ADMIN_, ADMIN_.ID.eq(id)) ?: throw BusinessException("用户不存在")


        // 2. 获取权限
        return LoginAdmin(
            id = record.id!!,
            permissions = record.permissionKey,
            username = record.username!!,
            password = record.password!!,
            authorities = listOf()
        )
    }
}