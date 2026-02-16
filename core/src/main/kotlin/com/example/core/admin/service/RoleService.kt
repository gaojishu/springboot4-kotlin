package com.example.core.admin.service

import com.example.data.admin.entity.RoleEntity
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.core.admin.dto.req.role.RoleCreateReq
import com.example.core.admin.dto.req.role.RoleUpdateReq
import com.example.core.admin.dto.res.role.RoleItemRes

/**
 * @author xkl
 * @since 2026-02-12
 */
interface RoleService : IService<RoleEntity>{
    fun getAll(): List<RoleItemRes>
    fun create(req: RoleCreateReq): RoleItemRes
    fun updateById(req: RoleUpdateReq): RoleItemRes
    fun deleteById(id: Long): Boolean
}
