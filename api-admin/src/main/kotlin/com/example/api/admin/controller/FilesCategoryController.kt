package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.files.category.FilesCategoryCreateReq
import com.example.core.admin.dto.req.files.category.FilesCategoryUpdateReq
import com.example.core.admin.dto.res.files.category.FilesCategoryItemRes
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import com.example.core.admin.service.FilesCategoryService

@RestController
@RequestMapping("/files_category")
class FilesCategoryController{
    @Autowired
    lateinit var filesCategoryService: FilesCategoryService

    @PostMapping("/create")
    fun create(@RequestBody req: FilesCategoryCreateReq): ApiResult<Unit> {
        filesCategoryService.create(req)
        return ApiResult.ok<Unit>().message("操作成功")
    }

    @PostMapping("/update")
    fun update(@RequestBody req: FilesCategoryUpdateReq): ApiResult<Unit> {
        filesCategoryService.updateById(req)
        return ApiResult.ok<Unit>().message("操作成功")
    }

    @GetMapping("/delete")
    fun delete(@RequestParam id: Long): ApiResult<Unit> {
        filesCategoryService.deleteById(id)
        return ApiResult.ok<Unit>().message("操作成功")
    }

    @GetMapping("/list")
    fun list(): ApiResult<List<FilesCategoryItemRes>> {
        val res = filesCategoryService.getAll()
        return ApiResult.ok<List<FilesCategoryItemRes>>().data(res)
    }
}
