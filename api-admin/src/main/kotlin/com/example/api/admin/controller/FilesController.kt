package com.example.api.admin.controller

import com.example.api.admin.dto.ApiResult
import com.example.core.admin.dto.req.files.FilesCreateReq
import com.example.core.admin.dto.req.files.FilesDeleteReq
import com.example.core.admin.dto.req.files.FilesQueryReq
import com.example.core.admin.dto.req.files.FilesUpdateReq
import com.example.core.admin.dto.res.files.FilesItemRes
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import com.example.core.admin.service.FilesService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize


@PreAuthorize("hasAuthority('file:all')")
@RestController
@RequestMapping("/files")
class FilesController{
    @Autowired
    lateinit var filesService: FilesService

    @PostMapping("/create")
    fun create(@RequestBody req: FilesCreateReq): ApiResult<Unit> {
        filesService.create(req)
        return ApiResult.ok<Unit>().message("上传成功")
    }

    @PostMapping("/update")
    fun update(@RequestBody req: FilesUpdateReq): ApiResult<Unit> {
        filesService.updateById(req)
        return ApiResult.ok<Unit>().message("更新成功")
    }

    @PostMapping("/delete")
    fun delete( @RequestBody req: FilesDeleteReq): ApiResult<Unit> {
        filesService.deleteByKey(req)
        return ApiResult.ok<Unit>().message("删除成功")
    }

    @PostMapping("/page")
    fun page(@Valid @RequestBody req: FilesQueryReq): ApiResult<Page<FilesItemRes>> {
        val page = filesService.page(req)
        return ApiResult.ok<Page<FilesItemRes>>().data(page)
    }

    @GetMapping("/hash")
    fun hash(@RequestParam hash: String): ApiResult<FilesItemRes?> {
        val res = filesService.getByHash(hash)
        return ApiResult.ok<FilesItemRes?>().data(res)
    }

}
