package com.example.core.admin.service.impl

import com.example.base.exception.BusinessException
import com.example.base.provider.storage.StorageProvider
import com.example.core.admin.dto.req.files.FilesCreateReq
import com.example.core.admin.dto.req.files.FilesDeleteReq
import com.example.core.admin.dto.req.files.FilesQueryReq
import com.example.core.admin.dto.req.files.FilesUpdateReq
import com.example.core.admin.dto.res.files.FilesItemRes
import com.example.core.admin.dto.res.files.category.FilesCategoryItemRes
import org.springframework.stereotype.Service
import com.example.core.admin.service.FilesService
import com.example.data.extension.paginate
import com.example.data.admin.enums.SortEnum
import com.example.data.admin.enums.files.FilesTypeEnum
import com.example.data.jooq.tables.references.FILES
import com.example.data.jooq.tables.references.FILES_CATEGORY
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.SortField
import org.jooq.impl.DSL
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class FilesServiceImpl(
    private val dsl: DSLContext,
    private val storageProvider: StorageProvider
): FilesService {
    private fun buildCondition(params: FilesQueryReq.Params): Condition {

        var condition = DSL.noCondition()
        if(params.name != null){
            condition = condition.and(FILES.NAME.like("%${params.name}%"))
        }
        if(params.categoryId != null){
            condition = condition.and(FILES.CATEGORY_ID.eq(params.categoryId))
        }
        params.createdAt?.takeIf { it.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(FILES.CREATED_AT.between(
                    LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
            }
        }


        return condition
    }

    private fun buildOrderBy(sort: FilesQueryReq.Sort?): List<SortField<*>> {
        val sortFields = mutableListOf<SortField<*>>()

        if(sort?.id == null){
            sortFields.add(FILES.ID.desc())
        }else{
            sortFields.add(if (sort.id == SortEnum.ASCEND) FILES.ID.asc() else FILES.ID.desc())
        }


        return sortFields
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(req: FilesCreateReq) {
        req.fileList.forEach { file ->
            val typePrefix = file.mimeType.substringBefore('/')
            val fileType = FilesTypeEnum.fromValue(typePrefix)
            val newKey = storageProvider.rename(file.key, fileType.value)

            storageProvider.copy(file.key, newKey)
            dsl.insertInto(FILES)
                .set(FILES.NAME, file.name)
                .set(FILES.CATEGORY_ID, file.categoryId)
                .set(FILES.KEY, newKey)
                .set(FILES.MIME_TYPE, file.mimeType)
                .set(FILES.HASH, file.hash)
                .set(FILES.TYPE, fileType)
                .set(FILES.SIZE, file.size)
                .execute()
        }
    }

    override fun updateById(req: FilesUpdateReq) {
        dsl.update(FILES)
            .set(FILES.NAME, req.name)
            .set(FILES.CATEGORY_ID, req.categoryId)
            .where(FILES.ID.eq(req.id))
            .execute()
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun deleteByKey(req: FilesDeleteReq) {
        req.keys.forEach { key ->
            val path = UriComponentsBuilder.fromUriString(key).build().path?.removePrefix("/")
            val record = dsl.fetchOne(FILES, FILES.KEY.eq(path))
                ?: throw BusinessException("文件不存在")
            record.delete()
        }
        req.keys.forEach { key->
            val path = UriComponentsBuilder.fromUriString(key).build().path?.removePrefix("/")
            path?.let { storageProvider.delete(it) }
        }
    }

    override fun page(req: FilesQueryReq): Page<FilesItemRes> {
        val pageable = PageRequest.of(req.params.current - 1, req.params.pageSize)
        val condition = buildCondition(req.params)
        val orderBy = buildOrderBy(req.sort)
        val res = dsl.paginate(
            countTable = FILES,
            condition = condition,
            pageable = pageable,
        ){ context ->
            context.select(
                *FILES.fields(),
                *FILES_CATEGORY.fields()
            )
                .from(FILES)
                .leftJoin(FILES_CATEGORY).on(FILES.CATEGORY_ID.eq(FILES_CATEGORY.ID))
                .orderBy(orderBy)
        }

        return res.map{ record->
            // 1. 自动提取 FILES 表相关的列，映射到主 DTO
            val item = record.into(FILES).into(FilesItemRes::class.java)
            item.key = storageProvider.generatePresignedUrl(item.key!!)

            // 2. 自动提取 FILES_CATEGORY 表相关的列
            // jOOQ 会根据列名自动匹配，即 FILES_CATEGORY.ID, FILES_CATEGORY.NAME 等
            val categoryRecord = record.into(FILES_CATEGORY)

            // 3. 如果关联字段为空（左连接未匹配到），则 category 为 null
            val category = if (categoryRecord.id != null) {
                categoryRecord.into(FilesCategoryItemRes::class.java)
            } else null

            // 4. 组装嵌套结构
            item.copy(category = category)
        }
    }

    override fun getByHash(hash: String): FilesItemRes? {
        val record = dsl.selectFrom(FILES)
            .where(FILES.HASH.eq(hash))
            .fetchOne()
        return record?.into(FilesItemRes::class.java)
    }

}
