package com.example.api.admin.controller

import com.baomidou.mybatisplus.core.metadata.IPage
import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.admin.AdminCreateReq
import com.example.core.admin.dto.req.admin.AdminQueryReq
import com.example.core.admin.dto.req.admin.AdminUpdateReq
import com.example.core.admin.dto.res.admin.AdminItemRes
import com.example.core.admin.service.AdminService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AdminController{
    @Autowired
    private lateinit var adminService: AdminService

    @PostMapping("/page")
    fun page(@Valid @RequestBody req: AdminQueryReq): ApiResult<IPage<AdminItemRes>> {
        val page = adminService.page(req)
        return ApiResult.ok<IPage<AdminItemRes>>().data(page)
    }

    @PostMapping("/delete")
    fun delete(id: Long): ApiResult<Unit> {
        adminService.deleteById(id)
        return ApiResult.ok<Unit>().message("操作成功")
    }

    @PostMapping("/create")
    fun create(@RequestBody req: AdminCreateReq): ApiResult<Unit> {
        adminService.create(req)
        return ApiResult.ok<Unit>().message("操作成功")
    }

    @PostMapping("/update")
    fun update(@RequestBody req: AdminUpdateReq): ApiResult<Unit> {
        adminService.updateById(req)
        return ApiResult.ok<Unit>()
    }

}
