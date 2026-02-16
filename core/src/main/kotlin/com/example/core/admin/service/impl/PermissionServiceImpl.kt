package com.example.core.admin.service.impl

import com.example.base.exception.BusinessException
import com.example.core.admin.dto.req.permission.PermissionCreateReq
import com.example.core.admin.dto.req.permission.PermissionUpdateReq
import com.example.core.admin.dto.res.admin.AdminItemRes
import com.example.core.admin.dto.res.permission.PermissionItemRes
import com.example.core.admin.service.PermissionService
import org.springframework.stereotype.Service
import com.example.data.generated.admin.tables.references.ADMIN_
import com.example.data.generated.admin.tables.references.ADMIN_PERMISSION
import com.example.data.generated.admin.tables.references.PERMISSION
import org.jooq.DSLContext
import org.jooq.impl.DSL.multiset
import org.jooq.impl.DSL.select

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
            .fetchInto(PermissionItemRes::class.java)

        return records
    }

    override fun create(req: PermissionCreateReq): Boolean {
        val res = dsl.insertInto(PERMISSION)
            .set(PERMISSION.ICON, req.icon)
            .set(PERMISSION.NAME, req.name)
            .set(PERMISSION.PARENT_ID, req.parentId)
            .set(PERMISSION.PATH, req.path)
            .set(PERMISSION.REMARK, req.remark)
            .set(PERMISSION.SORT, req.sort)
            .set(PERMISSION.TYPE, req.type)
            .set(PERMISSION.CODE, req.code)
            .execute()
        return res > 0
    }

    override fun updateById(req: PermissionUpdateReq): Boolean {
        val record = dsl.fetchOne(PERMISSION)?:throw BusinessException("权限不存在")

        record.icon = req.icon
        record.name = req.name
        record.parentId = req.parentId
        record.path = req.path
        record.remark = req.remark
        record.sort = req.sort
        record.type = req.type
        record.code = req.code

        val res = record.store()

        return res > 0
    }

    override fun deleteById(id: Long): Boolean {
        val record = dsl.fetchOne(PERMISSION, PERMISSION.ID.eq(id))?: throw BusinessException("权限不存在")

        val child = dsl.fetchOne(PERMISSION, PERMISSION.PARENT_ID.eq(id))

        if (child != null) {
            throw BusinessException("请先删除子权限")
        }
        val res = record.delete()
        return res > 0
    }
}
