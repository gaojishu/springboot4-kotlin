package com.example.core.admin.service

import com.example.data.admin.entity.PermissionEntity
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.core.admin.dto.req.permission.PermissionCreateReq
import com.example.core.admin.dto.req.permission.PermissionUpdateReq
import com.example.core.admin.dto.res.permission.PermissionItemRes

/**
 * @author xkl
 * @since 2026-02-12
 */
interface PermissionService : IService<PermissionEntity>{
    fun getAll(): List<PermissionItemRes>
    fun create(req: PermissionCreateReq): Boolean
    fun updateById(req: PermissionUpdateReq): Boolean
    fun deleteById(id: Long): Boolean
    fun selectByAdminId(adminId: Long): List<PermissionItemRes>
}
