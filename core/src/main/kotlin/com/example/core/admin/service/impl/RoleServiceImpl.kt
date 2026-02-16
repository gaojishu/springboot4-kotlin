package com.example.core.admin.service.impl

import com.example.base.exception.BusinessException
import com.example.core.admin.dto.req.role.RoleCreateReq
import com.example.core.admin.dto.req.role.RoleUpdateReq
import com.example.core.admin.dto.res.role.RoleItemRes
import com.example.core.admin.service.RoleService
import com.example.data.generated.admin.tables.references.ROLE
import org.jooq.DSLContext
import org.springframework.stereotype.Service

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class RoleServiceImpl(
    private val dsl: DSLContext
): RoleService {
    override fun getAll(): List<RoleItemRes> {
        val res = dsl.selectFrom(ROLE).fetchInto(RoleItemRes::class.java)

        return res
    }

    override fun create(req: RoleCreateReq): RoleItemRes? {
        val res = dsl.insertInto(ROLE)
            .set(ROLE.NAME, req.name)
            .set(ROLE.REMARK, req.remark)
            .set(ROLE.PERMISSION_KEY, req.permissionKey)
            .returning()
            .fetchOneInto(RoleItemRes::class.java)
        return res
    }

    override fun updateById(req: RoleUpdateReq): RoleItemRes {
        val record = dsl.fetchOne(ROLE, ROLE.ID.eq(req.id))?: throw BusinessException("模板不存在")

        record.name = req.name
        record.remark = req.remark
        record.permissionKey = req.permissionKey
        record.store()

        return record.into(RoleItemRes::class.java)
    }

    override fun deleteById(id: Long): Boolean {
        val record = dsl.fetchOne(ROLE, ROLE.ID.eq(id)) ?: throw BusinessException("模板不存在")
        return record.delete() > 0
    }
}
