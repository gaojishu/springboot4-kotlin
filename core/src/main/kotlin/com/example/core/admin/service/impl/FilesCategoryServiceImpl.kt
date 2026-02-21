package com.example.core.admin.service.impl

import com.example.base.exception.BusinessException
import com.example.core.admin.dto.req.files.category.FilesCategoryCreateReq
import com.example.core.admin.dto.req.files.category.FilesCategoryUpdateReq
import com.example.core.admin.dto.res.files.category.FilesCategoryItemRes
import org.springframework.stereotype.Service
import com.example.core.admin.service.FilesCategoryService
import com.example.data.jooq.tables.references.FILES
import com.example.data.jooq.tables.references.FILES_CATEGORY
import org.jooq.DSLContext

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class FilesCategoryServiceImpl(
    private val dsl: DSLContext
): FilesCategoryService {
    override fun create(req: FilesCategoryCreateReq){
        val record = dsl.newRecord(FILES_CATEGORY)
        record.name = req.name
        record.remark = req.remark
        record.store()
    }
    override fun updateById(req: FilesCategoryUpdateReq){
        val record = dsl.newRecord(FILES_CATEGORY)
        record.id = req.id
        record.name = req.name
        record.remark = req.remark
        record.store()
    }
    override fun deleteById(id: Long){
        val file = dsl.fetchOne(FILES, FILES.CATEGORY_ID.eq(id))
        if (file != null) {
            throw BusinessException("文件分类下有文件，请先删除文件")
        }

        dsl.deleteFrom(FILES_CATEGORY)
            .where(FILES_CATEGORY.ID.eq(id))
            .execute()
    }
    override fun getAll(): List<FilesCategoryItemRes>{
        return dsl.selectFrom(FILES_CATEGORY)
            .fetchInto(FilesCategoryItemRes::class.java)
    }

}
