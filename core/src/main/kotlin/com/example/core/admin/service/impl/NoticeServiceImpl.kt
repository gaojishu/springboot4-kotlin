package com.example.core.admin.service.impl

import com.example.base.provider.storage.StorageProvider
import com.example.core.admin.dto.req.notice.NoticeQueryReq
import com.example.core.admin.dto.res.notice.NoticeRes
import com.example.core.admin.security.LoginAdmin
import org.springframework.stereotype.Service
import com.example.core.admin.service.NoticeService
import com.example.data.extension.paginate
import com.example.data.admin.enums.SortEnum
import com.example.data.generated.admin.tables.records.NoticeRecord
import com.example.data.generated.admin.tables.references.NOTICE
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.SortField
import org.jooq.impl.DSL
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContextHolder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class NoticeServiceImpl(
    private val dsl: DSLContext,
    private val storageProvider: StorageProvider
): NoticeService {

    private fun buildCondition(params: NoticeQueryReq.Params): Condition {

        val authentication = SecurityContextHolder.getContext().authentication
        val admin = authentication?.principal as LoginAdmin


        var condition = DSL.noCondition()
        if(params.title != null){
            condition = condition.and(NOTICE.TITLE.like("%${params.title}%"))
        }
        condition = condition.and(NOTICE.ADMIN_ID.eq(admin.id))

        params.createdAt?.takeIf { it.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(NOTICE.CREATED_AT.between(
                    LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
            }
        }

        return condition
    }

    private fun buildOrderBy(sort: NoticeQueryReq.Sort?): List<SortField<*>> {
        val sortFields = mutableListOf<SortField<*>>()

        if(sort?.id == null){
            sortFields.add(NOTICE.ID.desc())
        }else{
            sortFields.add(if (sort.id == SortEnum.ASCEND) NOTICE.ID.asc() else NOTICE.ID.desc())
        }

        return sortFields
    }

    override fun create(
        adminId: Long?,
        title: String,
        content: String,
        attachments: List<String>?
    ){
        dsl.insertInto(NOTICE)
            .set(NOTICE.ADMIN_ID, adminId)
            .set(NOTICE.TITLE, title)
            .set(NOTICE.CONTENT, content)
            .set(NOTICE.ATTACHMENTS, attachments)
            .execute()
    }

    override fun page(req: NoticeQueryReq): Page<NoticeRes> {
        val pageable = PageRequest.of(req.params.current - 1, req.params.pageSize)
        val orderBy = buildOrderBy(req.sort)
        val condition = buildCondition(req.params)
        val res = dsl.paginate(
            countTable = NOTICE,
            condition = condition,
            pageable = pageable,
        ){ context ->
            context.selectFrom(NOTICE)
                .orderBy(orderBy)
        }
        return res.map{ record ->
            val row = record as NoticeRecord
            val processedAttachments = row.attachments?.map{
                storageProvider.generatePresignedUrl(it)
            }
            val dto = record.into(NoticeRes::class.java)
            dto.attachments = processedAttachments
            dto
        }
    }
}
