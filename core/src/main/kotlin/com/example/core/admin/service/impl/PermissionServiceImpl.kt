package com.example.core.admin.service.impl

import com.example.base.exception.BusinessException
import com.example.core.admin.dto.req.permission.PermissionCreateReq
import com.example.core.admin.dto.req.permission.PermissionUpdateReq
import com.example.core.admin.dto.res.admin.AdminItemRes
import com.example.core.admin.dto.res.permission.PermissionItemRes
import com.example.core.admin.service.PermissionService
import com.example.data.generated.admin.tables.records.PermissionRecord
import org.springframework.stereotype.Service
import com.example.data.generated.admin.tables.references.ADMIN_
import com.example.data.generated.admin.tables.references.ADMIN_PERMISSION
import com.example.data.generated.admin.tables.references.PERMISSION
import org.jooq.DSLContext
import org.jooq.impl.DSL.multiset
import org.jooq.impl.DSL.select
import org.springframework.transaction.annotation.Transactional

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class PermissionServiceImpl(
    private val dsl: DSLContext
): PermissionService {

    override fun selectByAdminId(adminId: Long): List<PermissionItemRes> {
        var records = emptyList<PermissionItemRes>()
        if(adminId != 1L){
            records = dsl.selectFrom(PERMISSION)
                .orderBy(PERMISSION.SORT.asc())
                .fetchInto(PermissionItemRes::class.java)

        }else{
            val admin = dsl.select(
                ADMIN_,
                multiset(
                    select(PERMISSION)
                        .from(PERMISSION)
                        .join(ADMIN_PERMISSION).on(PERMISSION.ID.eq(ADMIN_PERMISSION.PERMISSION_ID))
                        .where(ADMIN_PERMISSION.ADMIN_ID.eq(ADMIN_.ID))
                        .orderBy(PERMISSION.SORT.asc())

                ).`as`("permission")
                    .convertFrom { result -> result.into(PermissionItemRes::class.java) }
            ).from(ADMIN_)
                .where(ADMIN_.ID.eq(adminId))
                .fetchOneInto(AdminItemRes::class.java)

            admin?.let { records = it.permission }
        }

        return records
    }

    override fun getAll(): List<PermissionItemRes> {
        val records = dsl.selectFrom(PERMISSION)
            .orderBy(PERMISSION.SORT.asc())
            .fetchInto(PermissionItemRes::class.java)

        return records
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(req: PermissionCreateReq) {

        var parent: PermissionRecord? = null
        if (req.parentId != null) {
            parent = dsl.fetchOne(PERMISSION, PERMISSION.ID.eq(req.parentId))
        }

        val record = dsl.newRecord(PERMISSION)
        record.icon = req.icon
        record.name = req.name
        record.parentId = req.parentId
        record.path = req.path
        record.remark = req.remark
        record.sort = req.sort
        record.type = req.type
        record.code = req.code
        record.level = parent?.level?.plus(1) ?: 1
        record.store()

        val newId = record.id!!
        record.key = parent?.key?.let { "$it-$newId" } ?: newId.toString()

        record.store()
    }

    override fun updateById(req: PermissionUpdateReq) {
        var parent: PermissionRecord? = null
        if (req.parentId != null) {
            parent = dsl.fetchOne(PERMISSION, PERMISSION.ID.eq(req.parentId))
        }
        val record = dsl.fetchOne(PERMISSION, PERMISSION.ID.eq(req.id))?:throw BusinessException("权限不存在")
        record.icon = req.icon
        record.name = req.name
        record.parentId = req.parentId
        record.path = req.path
        record.remark = req.remark
        record.sort = req.sort
        record.type = req.type
        record.code = req.code
        record.level = parent?.level?.plus(1) ?: 1
        record.key = parent?.key?.let { "$it-${record.id}" } ?: record.id.toString()

        record.store()
    }

    override fun deleteById(id: Long) {
        val record = dsl.fetchOne(PERMISSION, PERMISSION.ID.eq(id))?: throw BusinessException("权限不存在")

        val child = dsl.fetchOne(PERMISSION, PERMISSION.PARENT_ID.eq(id))

        if (child != null) {
            throw BusinessException("请先删除子权限")
        }
        record.delete()
    }
}
