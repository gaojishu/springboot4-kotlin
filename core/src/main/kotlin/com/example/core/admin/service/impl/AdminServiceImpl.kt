package com.example.core.admin.service.impl

import com.example.core.admin.domain.service.CaptchaDomainService
import com.example.core.admin.domain.service.AdminDomainService
import com.example.core.admin.dto.req.admin.AdminLoginReq
import com.example.core.admin.dto.res.admin.AdminLoginRes
import com.example.core.admin.service.AdminService
import com.example.core.admin.service.TokenService
import org.springframework.stereotype.Service
import com.example.base.exception.BusinessException
import com.example.core.admin.dto.req.admin.AdminCreateReq
import com.example.core.admin.dto.req.admin.AdminUpdateReq
import com.example.core.admin.dto.req.admin.AdminQueryReq
import com.example.core.admin.dto.res.admin.AdminItemRes
import org.springframework.security.core.context.SecurityContextHolder
import com.example.core.extension.paginate
import com.example.data.admin.enums.SortEnum
import com.example.data.generated.admin.tables.references.ADMIN_
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.SortField
import org.jooq.impl.DSL
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.data.domain.Page

@Service
class AdminServiceImpl(
    private val tokenService: TokenService,
    private val adminDomainService: AdminDomainService,
    private val captchaDomainService: CaptchaDomainService,
    private val passwordEncoder: PasswordEncoder,
    private val dsl: DSLContext
): AdminService {

    private fun buildCondition(params: AdminQueryReq.Params): Condition {

        var condition = DSL.noCondition()

        params.username?.let {
            condition = condition.and(ADMIN_.USERNAME.contains(it))
        }
        params.mobile?.let {
            condition = condition.and(ADMIN_.MOBILE.contains(it))
        }
        params.email?.let {
            condition = condition.and(ADMIN_.EMAIL.contains(it))
        }

        params.disabledStatus?.let {
            condition = condition.and(ADMIN_.DISABLED_STATUS.eq(it))
        }
        params.nickname?.let {
            condition = condition.and(ADMIN_.NICKNAME.contains(it))
        }

        return condition
    }

    private fun buildOrderBy(sort: AdminQueryReq.Sort?): List<SortField<*>> {
        val sortFields = mutableListOf<SortField<*>>()

        if(sort?.id == null){
            sortFields.add(ADMIN_.ID.desc())
        }else{
            sortFields.add(if (sort.id == SortEnum.ASCEND) ADMIN_.ID.asc() else ADMIN_.ID.desc())
        }


        return sortFields
    }

    override fun page(req: AdminQueryReq): Page<AdminItemRes> {
        val pageable = PageRequest.of(req.params.current - 1, req.params.pageSize)
        val condition = buildCondition(req.params)
        val res = dsl.paginate(
            countTable = ADMIN_,
            condition = condition,
            pageable = pageable,
        ) {
            context ->
            context.select(ADMIN_)
            .from(ADMIN_)
            .where(condition)
            .orderBy(buildOrderBy(req.sort))
        }
        return res.map { record ->
            record.into(AdminItemRes::class.java)
        }
    }
    /**
     * 删除
     */
    override fun deleteById(id: Long) {
        dsl.deleteFrom(ADMIN_)
            .where(ADMIN_.ID.eq(id))
            .execute()
    }
    /**
     * 创建
     */
    override fun create(req: AdminCreateReq): Boolean {
        adminDomainService.validateUsernameAndPasswordFormat(
            req.username,
            req.password
        )
        val res = dsl.insertInto(ADMIN_)
            .set(ADMIN_.USERNAME, req.username)
            .set(ADMIN_.MOBILE, req.password)
            .set(ADMIN_.DISABLED_STATUS, req.disabledStatus)
            .set(ADMIN_.PASSWORD, passwordEncoder.encode(req.password).toString())
            .set(ADMIN_.EMAIL, req.email)
            .set(ADMIN_.NICKNAME, req.nickname)
            .set(ADMIN_.PERMISSION_KEY, req.permissionKey)
            .execute()

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

        val record = dsl.fetchOne(ADMIN_, ADMIN_.ID.eq(req.id)) ?: throw BusinessException("用户不存在")

        req.username?.let {
            record.username = it
        }

        req.password?.let {
            record.set(ADMIN_.PASSWORD, passwordEncoder.encode(it).toString())
        }
        req.disabledStatus?.let {
            record.set(ADMIN_.DISABLED_STATUS, it)
        }
        record.set(ADMIN_.EMAIL, req.email)
        record.set(ADMIN_.NICKNAME, req.nickname)
        record.set(ADMIN_.PERMISSION_KEY, req.permissionKey)

        val res = record.store()

        return res > 0
    }

    override fun logout() {
        SecurityContextHolder.clearContext()
    }

    override fun selectById(id: Long): AdminItemRes? {
        val record = dsl.fetchOne(ADMIN_, ADMIN_.ID.eq(id)) ?: throw BusinessException("用户不存在")

        return record.map {
            record ->
            record.into(AdminItemRes::class.java)
        }
    }


    override fun login(req: AdminLoginReq): AdminLoginRes {
        // 1. 验证码校验
        captchaDomainService.validateAndConsume(
            inputCode = req.captchaCode!!,
            uuid = req.captchaUuid!!
        )

        adminDomainService.validateUsernameAndPasswordFormat(
            req.username!!,
            req.password!!
        )

        val record = dsl.fetchOne(ADMIN_, ADMIN_.USERNAME.eq(req.username)) ?: throw BusinessException("用户不存在")

        // 2. 登录校验
        adminDomainService.verifyLogin(record, req.password)

        return tokenService.createToken(record.id!!)
    }

}
