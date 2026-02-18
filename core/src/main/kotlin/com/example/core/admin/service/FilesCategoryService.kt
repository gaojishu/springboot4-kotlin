package com.example.core.admin.service

import com.example.core.admin.dto.req.files.category.FilesCategoryCreateReq
import com.example.core.admin.dto.req.files.category.FilesCategoryUpdateReq
import com.example.core.admin.dto.res.files.category.FilesCategoryItemRes

/**
 * @author xkl
 * @since 2026-02-12
 */
interface FilesCategoryService {
    fun create(req: FilesCategoryCreateReq)
    fun updateById(req: FilesCategoryUpdateReq)
    fun deleteById(id: Long)
    fun getAll(): List<FilesCategoryItemRes>
}
