package com.example.core.admin.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.example.base.exception.BusinessException
import com.example.core.admin.converter.PermissionConverter
import com.example.core.admin.dto.req.permission.PermissionCreateReq
import com.example.core.admin.dto.req.permission.PermissionUpdateReq
import com.example.core.admin.dto.res.permission.PermissionItemRes
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.core.admin.service.PermissionService
import com.example.data.admin.entity.AdminPermissionEntity
import org.springframework.stereotype.Service;
import com.example.data.admin.mapper.PermissionMapper
import com.example.data.admin.entity.PermissionEntity
import com.example.data.admin.mapper.AdminPermissionMapper

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class PermissionServiceImpl(
    private val adminPermissionWrapper: AdminPermissionMapper,
): ServiceImpl<PermissionMapper, PermissionEntity>(), PermissionService {

    override fun selectByAdminId(adminId: Long): List<PermissionItemRes> {

        var per: List<PermissionEntity>
        if(adminId == 1L){
            per = baseMapper.selectList(
                KtQueryWrapper(PermissionEntity::class.java)
                    .orderByAsc(PermissionEntity::sort)
            )
        }else{
            val permissionIds = adminPermissionWrapper.selectList(
                KtQueryWrapper(AdminPermissionEntity::class.java)
                    .eq(AdminPermissionEntity::adminId, adminId)
            ).map { it.permissionId }

             per = if (permissionIds.isNotEmpty()) {
                baseMapper.selectList(
                    KtQueryWrapper(PermissionEntity::class.java)
                        .`in`(PermissionEntity::id, permissionIds)
                        .orderByAsc(PermissionEntity::sort)
                )
            } else {
                emptyList()
            }
        }

        return per.map { PermissionConverter.INSTANCE.toRes(it) }
    }

    override fun getAll(): List<PermissionItemRes> {
        return baseMapper.selectList(null).map { PermissionConverter.INSTANCE.toRes(it) }
    }

    override fun create(req: PermissionCreateReq): Boolean {
        val entity = PermissionEntity().apply {
            icon = req.icon
            name = req.name
            parentId = req.parentId
            path = req.path
            remark = req.remark
            sort = req.sort
            type = req.type
            code = req.code
        }
        val id = baseMapper.insert(entity)
        return id > 0
    }
    override fun updateById(req: PermissionUpdateReq): Boolean {
        baseMapper.selectById(req.id)?:throw BusinessException("权限不存在")

        val wrapper = this.ktUpdate()
            .eq(PermissionEntity::id, req.id)
            .set(PermissionEntity::icon, req.icon)
            .set(PermissionEntity::name, req.name)
            .set(PermissionEntity::parentId, req.parentId)
            .set(PermissionEntity::path, req.path)
            .set(PermissionEntity::remark, req.remark)
            .set(PermissionEntity::sort, req.sort)
            .set(PermissionEntity::type, req.type)
            .set(PermissionEntity::code, req.code)
            .update()

        return wrapper
    }

    override fun deleteById(id: Long): Boolean {
        val entity = baseMapper.selectById(id)
        if (entity == null) {
            throw BusinessException("权限不存在")
        }
        val child = this.ktQuery()
            .eq(PermissionEntity::parentId, id)

        if (child != null) {
            throw BusinessException("请先删除子权限")
        }
        return baseMapper.deleteById(id) > 0
    }
}
