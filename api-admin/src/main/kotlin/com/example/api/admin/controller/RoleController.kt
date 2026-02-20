package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.role.RoleCreateReq
import com.example.core.admin.dto.req.role.RoleUpdateReq
import com.example.core.admin.dto.res.role.RoleItemRes
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import com.example.core.admin.service.RoleService
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/role")
class RoleController{
    @Autowired
    lateinit var roleService: RoleService

    @PreAuthorize("hasAuthority('role:create')")
    @PostMapping("/create")
    fun create(@RequestBody req: RoleCreateReq): ApiResult<RoleItemRes> {
        val res = roleService.create(req)
        return ApiResult.ok<RoleItemRes>().data(res).message("创建成功")
    }


    @PreAuthorize("hasAuthority('role:update')")
    @PostMapping("/update")
    fun update(@RequestBody req: RoleUpdateReq): ApiResult<RoleItemRes> {
        val res = roleService.updateById(req)
        return ApiResult.ok<RoleItemRes>().data(res).message("更新成功")
    }


    @PreAuthorize("hasAuthority('role:delete')")
    @GetMapping("/delete")
    fun delete(@RequestParam id: Long): ApiResult<Unit> {
        roleService.deleteById(id)
        return ApiResult.ok<Unit>().message("删除成功")
    }


    @PreAuthorize("hasAuthority('role:read')")
    @GetMapping("/list")
    fun list(): ApiResult<List<RoleItemRes>> {
        val res = roleService.getAll()
        return ApiResult.ok<List<RoleItemRes>>().data(res)
    }

}
