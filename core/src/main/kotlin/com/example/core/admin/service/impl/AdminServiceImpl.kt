package com.example.core.admin.service.impl

import com.example.core.admin.domain.service.CaptchaDomainService
import com.example.core.admin.domain.service.AdminDomainService
import com.example.core.admin.dto.req.admin.AdminLoginReq
import com.example.core.admin.dto.res.admin.AdminLoginRes
import com.example.core.admin.service.AdminService
import com.example.core.admin.service.TokenService
import org.springframework.stereotype.Service
import com.example.data.admin.mapper.AdminMapper
import com.example.data.admin.entity.AdminEntity
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.base.exception.BusinessException
import com.example.core.admin.converter.AdminConverter
import com.example.core.admin.dto.req.admin.AdminCreateReq
import com.example.core.admin.dto.req.admin.AdminUpdateReq
import com.example.core.admin.dto.req.admin.AdminQueryReq
import com.example.core.admin.dto.res.admin.AdminItemRes
import com.example.core.admin.security.LoginAdmin
import org.springframework.security.core.context.SecurityContextHolder
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.security.crypto.password.PasswordEncoder

@Service
class AdminServiceImpl(
    private val tokenService: TokenService,
    private val adminDomainService: AdminDomainService,
    private val captchaDomainService: CaptchaDomainService,
    private val passwordEncoder: PasswordEncoder,
): ServiceImpl<AdminMapper, AdminEntity>(),AdminService {
    private fun buildWrapper(adminQueryReq: AdminQueryReq): KtQueryWrapper<AdminEntity> {
        val params = adminQueryReq.params
        val sort = adminQueryReq.sort

        val wrapper = KtQueryWrapper(AdminEntity::class.java)
            .like(params.username != null, AdminEntity::username, params.username)
            .like(params.mobile != null, AdminEntity::mobile, params.mobile)
            .like(params.email != null, AdminEntity::email, params.email)
            .like(params.nickname != null, AdminEntity::nickname, params.nickname)
            .eq(params.disabledStatus != null, AdminEntity::disabledStatus, params.disabledStatus)

        wrapper.orderBy(true,sort?.id == "ascend",AdminEntity::id)

        return wrapper
    }

    override fun page(adminQueryReq: AdminQueryReq): IPage<AdminItemRes> {
        val page = Page<AdminEntity>(
            adminQueryReq.params.current,
            adminQueryReq.params.pageSize
        )
        val buildWrapper = buildWrapper(adminQueryReq)
        val wrapper = baseMapper.selectPage(page, buildWrapper)

        return wrapper.convert { AdminConverter.INSTANCE.toRes(it) }
    }
    /**
     * 删除
     */
    override fun deleteById(id: Long) {
        baseMapper.deleteById(id)
    }
    /**
     * 创建
     */
    override fun create(adminCreateReq: AdminCreateReq): AdminItemRes? {
        adminDomainService.validateUsernameAndPasswordFormat(
            adminCreateReq.username,
            adminCreateReq.password
        )
        val admin = AdminEntity().apply {
            adminCreateReq.username.let{ username = it}
            adminCreateReq.password.let{ password = passwordEncoder.encode(it).toString() }
            adminCreateReq.disabledStatus.let{ disabledStatus = it }
            adminCreateReq.mobile?.let{ mobile = it }
            adminCreateReq.email?.let{ email = it }
            adminCreateReq.nickname?.let{ nickname = it }
            adminCreateReq.permissionKey?.let{ permissionKey = it }
        }
        baseMapper.insert(admin)
        return admin.let { AdminConverter.INSTANCE.toRes(admin) }
    }

    override fun updateById(adminUpdateReq: AdminUpdateReq): AdminItemRes? {

        adminUpdateReq.username?.let {
            adminDomainService.validateUsernameFormat(
                it
            )
        }
        adminUpdateReq.password?.let {
            adminDomainService.validatePasswordFormat(
                it
            )
        }
        val admin = baseMapper.selectById(adminUpdateReq.id)
        if (admin == null) {
            throw BusinessException("用户不存在")
        }
        admin.apply {
            adminUpdateReq.username?.let{ username = it }
            adminUpdateReq.password?.let{ password = passwordEncoder.encode(it).toString() }
            adminUpdateReq.disabledStatus?.let{ disabledStatus = it }
            adminUpdateReq.mobile?.let{ mobile = it }
            adminUpdateReq.email?.let{ email = it }
            adminUpdateReq.nickname?.let{ nickname = it }
            adminUpdateReq.permissionKey?.let{ permissionKey = it }
        }
        return admin.let { AdminConverter.INSTANCE.toRes(admin) }
    }

    override fun logout() {
        SecurityContextHolder.clearContext()
    }
    override fun selectById(id: Long): AdminItemRes? {
        val admin = baseMapper.selectById(id)
        return admin?.let { AdminConverter.INSTANCE.toRes(admin) }
    }

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
