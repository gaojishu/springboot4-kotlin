package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.permission.PermissionCreateReq
import com.example.core.admin.dto.req.permission.PermissionUpdateReq
import com.example.core.admin.dto.res.permission.PermissionItemRes
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import com.example.core.admin.service.PermissionService

@RestController
@RequestMapping("/permission")
class PermissionController{
    @Autowired
    lateinit var permissionService: PermissionService

    @GetMapping("/list")
    fun list(): ApiResult<List<PermissionItemRes>> {
        val list = permissionService.getAll()
        return ApiResult.ok<List<PermissionItemRes>>().data(list)

    }
    @PostMapping("/create")
    fun create(@RequestBody req: PermissionCreateReq): ApiResult<Unit> {
        permissionService.create(req)
        return ApiResult.ok<Unit>().message("操作成功")
    }
    @PostMapping("/update")
    fun update(@RequestBody req: PermissionUpdateReq): ApiResult<Unit> {
        permissionService.updateById(req)
        return ApiResult.ok<Unit>().message("操作成功")
    }

    @GetMapping("/delete")
    fun delete(@RequestParam id: Long): ApiResult<Unit> {
         permissionService.deleteById(id)
        return ApiResult.ok<Unit>().message("操作成功")
    }

}
