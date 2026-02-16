package com.example.core.admin.service

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.service.IService
import com.example.core.admin.dto.req.admin.AdminCreateReq
import com.example.core.admin.dto.req.admin.AdminUpdateReq
import com.example.core.admin.dto.res.admin.AdminLoginRes
import com.example.core.admin.dto.req.admin.AdminLoginReq
import com.example.core.admin.dto.res.admin.AdminItemRes
import com.example.core.admin.dto.req.admin.AdminQueryReq
import com.example.data.admin.entity.AdminEntity

interface AdminService: IService<AdminEntity>  {
    fun login(req: AdminLoginReq): AdminLoginRes
    fun logout()
    fun selectById(id: Long): AdminItemRes?
    fun page(req: AdminQueryReq): IPage<AdminItemRes>
    fun deleteById(id: Long)
    fun create(req: AdminCreateReq): Boolean
    fun updateById(req: AdminUpdateReq): Boolean
}