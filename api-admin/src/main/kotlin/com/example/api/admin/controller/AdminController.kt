package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.admin.AdminCreateReq
import com.example.core.admin.dto.req.admin.AdminQueryReq
import com.example.core.admin.dto.req.admin.AdminUpdateReq
import com.example.core.admin.dto.res.admin.AdminItemRes
import com.example.core.admin.service.AdminService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AdminController{
    @Autowired
    private lateinit var adminService: AdminService

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/detail")
    fun detail(id: Long): ApiResult<AdminItemRes> {
        val res = adminService.selectById(id)
        return ApiResult.ok<AdminItemRes>().data(res)
    }

    @PreAuthorize("hasAuthority('admin:read')")
    @PostMapping("/page")
    fun page(@Valid @RequestBody req: AdminQueryReq): ApiResult<Page<AdminItemRes>> {
        val page = adminService.page(req)
        return ApiResult.ok<Page<AdminItemRes>>().data(page)
    }

    @PreAuthorize("hasAuthority('admin:delete')")
    @GetMapping("/delete")
    fun delete(id: Long): ApiResult<Unit> {
        adminService.deleteById(id)
        return ApiResult.ok<Unit>().message("操作成功")
    }

    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping("/create")
    fun create(@RequestBody req: AdminCreateReq): ApiResult<Unit> {
        adminService.create(req)
        return ApiResult.ok<Unit>().message("操作成功")
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @PostMapping("/update")
    fun update(@RequestBody req: AdminUpdateReq): ApiResult<Unit> {
        adminService.updateById(req)
        return ApiResult.ok<Unit>().message("操作成功")
    }

}
