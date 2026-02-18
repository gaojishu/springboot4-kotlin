package com.example.core.admin.service

import com.example.core.admin.dto.req.permission.PermissionCreateReq
import com.example.core.admin.dto.req.permission.PermissionUpdateReq
import com.example.core.admin.dto.res.permission.PermissionItemRes

/**
 * @author xkl
 * @since 2026-02-12
 */
interface PermissionService{
    fun getAll(): List<PermissionItemRes>
    fun create(req: PermissionCreateReq)
    fun updateById(req: PermissionUpdateReq)
    fun deleteById(id: Long)
    fun selectByAdminId(adminId: Long): List<PermissionItemRes>
}
