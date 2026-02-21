package com.example.core.admin.service.impl

import com.example.core.admin.dto.bo.op.log.OpLogQueryBO
import com.example.core.admin.dto.req.op.log.OpLogQueryReq
import org.springframework.stereotype.Service
import com.example.core.admin.service.OpLogService
import com.example.core.admin.dto.res.op.log.OpLogItemRes
import com.example.core.admin.query.OpLogQueryProvider
import com.example.core.admin.security.LoginAdmin
import com.example.data.extension.paginate
import com.example.data.jooq.tables.references.OP_LOG
import org.jooq.DSLContext
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.parameters.JobParametersBuilder
import org.springframework.batch.core.launch.JobOperator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContextHolder
import tools.jackson.databind.ObjectMapper
import java.util.UUID

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class OpLogServiceImpl(
    private val dsl: DSLContext,
    private val queryProvider: OpLogQueryProvider,
    @Qualifier("asyncJobOperator") private val jobOperator: JobOperator,
    private val opLogExportJob: Job,
    private val objectMapper: ObjectMapper,
): OpLogService {

    override fun create(
        adminId: Long?,
        ip: String?,
        method: String?,
        remark: String?,
        duration: Long,
        uri: String?,
        params: String?,
        queryParams: String?
    ) {
        dsl.insertInto(OP_LOG)
            .set(OP_LOG.ADMIN_ID, adminId)
            .set(OP_LOG.DURATION, duration)
            .set(OP_LOG.IP, ip)
            .set(OP_LOG.METHOD, method)
            .set(OP_LOG.REMARK, remark)
            .set(OP_LOG.URI, uri)
            .set(OP_LOG.PARAMS, params)
            .set(OP_LOG.QUERY_PARAMS, queryParams)
            .execute()

    }

    override fun export(req: OpLogQueryReq): Long {
        val opLogQueryBO = OpLogQueryBO(
            params = OpLogQueryBO.Params(
                remark = req.params.remark,
                method = req.params.method,
                uri = req.params.uri,
                ip = req.params.ip,
                adminId = req.params.adminId,
                createdAt = req.params.createdAt,
                updatedAt = req.params.updatedAt
            ),
            sort = OpLogQueryBO.Sort(
                id = req.sort?.id
            )
        )
        val reqJson = objectMapper.writeValueAsString(opLogQueryBO)
        val authentication = SecurityContextHolder.getContext().authentication
        val admin = authentication?.principal as LoginAdmin

        // 1. 构建 JobParameters
        val jobParameters = JobParametersBuilder()
            .addString("opLogQueryBO", reqJson)
            .addString("username", admin.username)
            .addLong("adminId", admin.id)
            .addString("uuid", UUID.randomUUID().toString())
            .toJobParameters()
        val job = jobOperator.start(opLogExportJob, jobParameters)
        return job.id
    }

    override fun page(req: OpLogQueryReq): Page<OpLogItemRes> {
        val pageable = PageRequest.of(req.params.current - 1, req.params.pageSize)

        val condition = queryProvider.buildCondition(OpLogQueryBO.Params(
            remark = req.params.remark,
            method = req.params.method,
            uri = req.params.uri,
            ip = req.params.ip,
            adminId = req.params.adminId,
            createdAt = req.params.createdAt,
            updatedAt = req.params.updatedAt
        ))
        val orderBy = queryProvider.buildOrderBy(OpLogQueryBO.Sort(
            id = req.sort?.id
        ))
        val res = dsl.paginate(
            countTable = OP_LOG,
            condition = condition,
            pageable = pageable,
        ){
                context ->
            context.select(OP_LOG)
                .from(OP_LOG)
                .where(condition)
                .orderBy(orderBy)
        }

        return res.map { record ->
            record.into(OpLogItemRes::class.java)
        }
    }

}
