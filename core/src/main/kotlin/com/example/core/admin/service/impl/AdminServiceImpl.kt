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
    private fun buildWrapper(req: AdminQueryReq): KtQueryWrapper<AdminEntity> {
        val params = req.params
        val sort = req.sort

        val wrapper = KtQueryWrapper(AdminEntity::class.java)
            .like(params.username != null, AdminEntity::username, params.username)
            .like(params.mobile != null, AdminEntity::mobile, params.mobile)
            .like(params.email != null, AdminEntity::email, params.email)
            .like(params.nickname != null, AdminEntity::nickname, params.nickname)
            .eq(params.disabledStatus != null, AdminEntity::disabledStatus, params.disabledStatus)

        wrapper.orderBy(true,sort?.id == "ascend",AdminEntity::id)

        return wrapper
    }

    override fun page(req: AdminQueryReq): IPage<AdminItemRes> {
        val page = Page<AdminEntity>(
            req.params.current,
            req.params.pageSize
        )
        val buildWrapper = buildWrapper(req)
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
    override fun create(req: AdminCreateReq): Boolean {
        adminDomainService.validateUsernameAndPasswordFormat(
            req.username,
            req.password
        )
        val admin = AdminEntity().apply {
            username = req.username
            password = passwordEncoder.encode(req.password).toString()
            mobile = req.mobile
            email = req.email
            nickname = req.nickname
            disabledStatus = req.disabledStatus
        }
        val res = baseMapper.insert(admin)
        return res > 0
    }

    override fun updateById(req: AdminUpdateReq): Boolean {

        req.username?.let {
            adminDomainService.validateUsernameFormat(
                it
            )
        }
        req.password?.let {
            adminDomainService.validatePasswordFormat(
                it
            )
        }

      val entity =  this.ktUpdate().apply {
            eq(AdminEntity::id, req.id)
            set(req.username != null, AdminEntity::username, req.username)
            set(req.password != null, AdminEntity::password, passwordEncoder.encode(req.password).toString())
            set(req.disabledStatus != null, AdminEntity::disabledStatus, req.disabledStatus)
            set(AdminEntity::mobile, req.mobile)
            set(AdminEntity::email, req.email)
            set(AdminEntity::nickname, req.nickname)
            set(AdminEntity::permissionKey, req.permissionKey)
        }.update()
        return entity
    }

    override fun logout() {
        SecurityContextHolder.clearContext()
    }
    override fun selectById(id: Long): AdminItemRes? {
        val admin = baseMapper.selectById(id)
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
