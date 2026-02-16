package com.example.core.admin.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.example.base.exception.BusinessException
import com.example.core.admin.converter.RoleConverter
import com.example.core.admin.dto.req.role.RoleCreateReq
import com.example.core.admin.dto.req.role.RoleUpdateReq
import com.example.core.admin.dto.res.role.RoleItemRes
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.core.admin.service.RoleService
import org.springframework.stereotype.Service;
import com.example.data.admin.mapper.RoleMapper
import com.example.data.admin.entity.RoleEntity

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class RoleServiceImpl(
): ServiceImpl<RoleMapper, RoleEntity>(), RoleService {
    override fun getAll(): List<RoleItemRes> {
        val list = baseMapper.selectList(
            KtQueryWrapper(RoleEntity::class.java)
                .orderByAsc(RoleEntity::id)
        )

        return list.map { RoleConverter.INSTANCE.toRes(it) }
    }

    override fun create(req: RoleCreateReq): RoleItemRes {
        val entity = RoleEntity()
        entity.name = req.name
        entity.remark = req.remark
        entity.permissionKey = req.permissionKey
        baseMapper.insert(entity)
        return RoleConverter.INSTANCE.toRes(entity)
    }

    override fun updateById(req: RoleUpdateReq): RoleItemRes {
        val entity = baseMapper.selectById(req.id)
        if (entity == null) {
            throw BusinessException("角色不存在")
        }
        this.ktUpdate()
            .eq(RoleEntity::id, req.id)
            .set(RoleEntity::name, req.name)
            .set(RoleEntity::remark, req.remark)
            .set(RoleEntity::permissionKey, req.permissionKey)
            .update()
        return RoleConverter.INSTANCE.toRes(entity)
    }

    override fun deleteById(id: Long): Boolean {
        val entity = baseMapper.selectById(id)
        if (entity == null) {
            throw BusinessException("角色不存在")
        }
        return baseMapper.deleteById(id) > 0
    }
}
