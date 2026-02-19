package com.example.core.admin.service.impl

import com.example.core.admin.domain.service.AdminDomainService
import com.example.core.admin.dto.req.admin.AdminLoginReq
import com.example.core.admin.dto.res.admin.AdminLoginRes
import com.example.core.admin.service.AdminService
import org.springframework.stereotype.Service
import com.example.base.exception.BusinessException
import com.example.base.provider.captcha.CaptchaProvider
import com.example.base.provider.token.TokenProvider
import com.example.core.admin.dto.req.admin.AdminCreateReq
import com.example.core.admin.dto.req.admin.AdminUpdateReq
import com.example.core.admin.dto.req.admin.AdminQueryReq
import com.example.core.admin.dto.res.admin.AdminItemRes
import org.springframework.security.core.context.SecurityContextHolder
import com.example.data.extension.paginate
import com.example.data.admin.enums.SortEnum
import com.example.data.generated.admin.tables.references.ADMIN_
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.SortField
import org.jooq.impl.DSL
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.data.domain.Page
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class AdminServiceImpl(
    private val tokenProvider: TokenProvider,
    private val adminDomainService: AdminDomainService,
    private val captchaProvider: CaptchaProvider,
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
        params.createdAt?.takeIf { it.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(ADMIN_.CREATED_AT.between(
                    LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
            }
        }
        params.updatedAt?.takeIf { it.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(ADMIN_.UPDATED_AT.between(
                    LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
            }
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
    override fun create(req: AdminCreateReq) {
        adminDomainService.validateUsernameAndPasswordFormat(
            req.username,
            req.password
        )
        dsl.insertInto(ADMIN_)
            .set(ADMIN_.USERNAME, req.username)
            .set(ADMIN_.MOBILE, req.mobile)
            .set(ADMIN_.DISABLED_STATUS, req.disabledStatus)
            .set(ADMIN_.PASSWORD, passwordEncoder.encode(req.password).toString())
            .set(ADMIN_.EMAIL, req.email)
            .set(ADMIN_.NICKNAME, req.nickname)
            .set(ADMIN_.PERMISSION_KEY, req.permissionKey)
            .execute()
    }

    override fun updateById(req: AdminUpdateReq) {

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
            record.password = passwordEncoder.encode(it)
        }
        req.disabledStatus?.let {
            record.disabledStatus = it
        }
        req.mobile?.let {
            record.mobile = it
        }
        req.email?.let {
            record.email = it
        }
        req.nickname?.let {
            record.nickname = it
        }
        req.permissionKey?.let {
            record.permissionKey = it
        }

        record.store()
    }

    override fun logout() {
        val auth = SecurityContextHolder.getContext().authentication
        val token = auth?.credentials as? String
        if (token != null) {
            tokenProvider.deleteToken(token)
        }
        // 清除当前线程的认证信息
        SecurityContextHolder.clearContext()
    }

    override fun selectByUsername(username: String): AdminItemRes {
        val record = dsl.fetchOne(ADMIN_, ADMIN_.USERNAME.eq(username)) ?: throw BusinessException("用户不存在")

        return record.map {
            record ->
            record.into(AdminItemRes::class.java)
        }
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
        captchaProvider.validateAndConsume(req.captchaCode!!, req.captchaUuid!!)

        adminDomainService.validateUsernameAndPasswordFormat(
            req.username!!,
            req.password!!
        )

        val record = dsl.fetchOne(ADMIN_, ADMIN_.USERNAME.eq(req.username)) ?: throw BusinessException("用户不存在")

        // 2. 登录校验
        adminDomainService.verifyLogin(record, req.password)

        val token = tokenProvider.createToken(record.id!!)

        return AdminLoginRes(
            token = token.token,
            expiredAt = token.expiredAt,
            header = "Authorization",
            prefix = "Bearer",
        )
    }

}
