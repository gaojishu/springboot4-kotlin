package com.example.core.admin.service.impl

import com.example.core.admin.domain.service.CaptchaDomainService
import com.example.core.admin.domain.service.AdminDomainService
import com.example.core.admin.dto.request.admin.AdminLoginReq
import com.example.core.admin.dto.response.admin.AdminLoginRes
import com.example.core.admin.service.AdminService
import com.example.core.admin.service.TokenService
import org.springframework.stereotype.Service
import com.example.data.admin.mapper.AdminMapper
import com.example.data.admin.entity.AdminEntity
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.base.exception.BusinessException
import com.example.core.admin.converter.AdminConverter
import com.example.core.admin.dto.response.admin.AdminItemRes
import com.example.core.admin.security.LoginAdmin
import org.springframework.security.core.context.SecurityContextHolder

@Service
class AdminServiceImpl(
    private val tokenService: TokenService,
    private val adminDomainService: AdminDomainService,
    private val captchaDomainService: CaptchaDomainService,
): ServiceImpl<AdminMapper, AdminEntity>(),AdminService {
    override fun info(): AdminItemRes? {
        val loginAdmin = SecurityContextHolder.getContext().authentication?.let { it.principal as? LoginAdmin }
        val adminId = loginAdmin?.adminId

        val admin = baseMapper.selectById(adminId)
        return admin?.let { AdminConverter.INSTANCE.toRes(admin) }
    }

    override fun login(adminLoginReq: AdminLoginReq): AdminLoginRes {
        // 1. 验证码校验
        captchaDomainService.validateAndConsume(
            inputCode = adminLoginReq.captchaCode!!,
            uuid = adminLoginReq.captchaUuid!!
        )

        adminDomainService.validateUsernameAndPasswordFormat(
            adminLoginReq.username!!,
            adminLoginReq.password!!
        )

        val admin = baseMapper.selectOne(
            KtQueryWrapper(AdminEntity::class.java)
                .eq(AdminEntity::username, adminLoginReq.username)
        )

        if (admin == null) {
            throw BusinessException("用户不存在")
        }

        // 2. 登录校验
        adminDomainService.verifyLogin(admin, adminLoginReq.password)

        return tokenService.createToken(admin.id!!)
    }

}
