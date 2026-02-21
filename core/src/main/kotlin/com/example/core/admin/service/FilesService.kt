package com.example.core.admin.service

import com.example.core.admin.dto.req.files.FilesCreateReq
import com.example.core.admin.dto.req.files.FilesDeleteReq
import com.example.core.admin.dto.req.files.FilesQueryReq
import com.example.core.admin.dto.req.files.FilesUpdateReq
import com.example.core.admin.dto.res.files.FilesItemRes
import org.springframework.data.domain.Page

/**
 * @author xkl
 * @since 2026-02-12
 */
interface FilesService {
    fun create(req: FilesCreateReq)
    fun updateById(req: FilesUpdateReq)
    fun deleteByKey(req: FilesDeleteReq)
    fun page(req: FilesQueryReq): Page<FilesItemRes>
    fun getByHash(hash: String): FilesItemRes?
}
