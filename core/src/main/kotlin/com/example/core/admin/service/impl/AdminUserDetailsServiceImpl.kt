package com.example.core.admin.service.impl

import com.example.base.exception.BusinessException
import com.example.core.admin.security.LoginAdmin
import com.example.core.admin.service.PermissionService
import com.example.data.jooq.tables.references.ADMIN_
import org.jooq.DSLContext
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AdminUserDetailsServiceImpl(
    private val dsl: DSLContext,
    private val permissionService: PermissionService
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(id: String): UserDetails {
        val id = id.toLong()
        val record = dsl.fetchOne(ADMIN_, ADMIN_.ID.eq(id)) ?: throw BusinessException("用户不存在")

        val permission = permissionService.selectByAdminId(id)
        val authorities = permission
            .filter { it.code != null } // 过滤掉 code 为 null 的权限
            .map { SimpleGrantedAuthority(it.code!!) } // 安全地解包非空的 code

        return LoginAdmin(
            id = record.id!!,
            username = record.username!!,
            password = record.password!!,
            disabled = false,
            authorities = authorities
        )
    }
}