package com.example.core.admin.service.impl

import com.example.base.exception.BusinessException
import com.example.core.admin.security.LoginAdmin
import com.example.data.admin.mapper.AdminMapper
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AdminUserDetailsServiceImpl(
    private val adminMapper: AdminMapper
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(id: String): UserDetails {
        val adminId = id.toLongOrNull()
        val admin = adminMapper.selectById(adminId) ?: throw BusinessException("用户不存在")
        // 2. 获取权限
        return LoginAdmin(
            adminId = admin.id!!,
            permissions = admin.permissionKey,
            username = admin.username,
            password = admin.password,
            authorities = listOf()
        )
    }
}