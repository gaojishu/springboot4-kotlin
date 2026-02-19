package com.example.api.admin.batch.op.log

import com.example.api.admin.batch.ExcelStreamingItemWriter
import com.example.api.admin.batch.ExportUploadListener
import com.example.api.admin.batch.JooqStreamingItemReader
import com.example.core.admin.dto.bo.op.log.OpLogQueryBO
import com.example.core.admin.dto.res.op.log.OpLogItemRes
import com.example.core.admin.query.OpLogQueryProvider
import com.example.data.generated.admin.tables.records.OpLogRecord
import com.example.data.generated.admin.tables.references.OP_LOG
import org.jooq.DSLContext
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.Step
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import tools.jackson.databind.ObjectMapper
import java.nio.file.Files

@Configuration
class OpLogExportConfig(
    private val queryProvider: OpLogQueryProvider,
    private val jobRepository: JobRepository,
    private val objectMapper: ObjectMapper,
) {



    @Bean
    fun opLogExportJob(
        opLogExportStep: Step,
        exportUploadListener: ExportUploadListener,
    ): Job {
        return JobBuilder("opLogExportJob", jobRepository)
            //.incrementer(RunIdIncrementer()) //RunIdIncrementer 会复用老参数
            .start(opLogExportStep)
            .listener(exportUploadListener)
            .build()
    }

    @Bean
    fun opLogExportStep(
        opLogReader: JooqStreamingItemReader<OpLogRecord,OpLogItemRes>,
        transactionManager: PlatformTransactionManager,
    ): Step {
        return StepBuilder("opLogExportStep", jobRepository)
            .chunk<OpLogItemRes, OpLogItemRes>(500)
            .reader(opLogReader)
            .writer(opLogExcelWriter())
            .transactionManager(transactionManager)
            .build()
    }

    @Bean
    @StepScope
    fun opLogReader(
        dsl: DSLContext,
        @Value("#{jobParameters[opLogQueryBO]}") queryJson: String?,
    ): JooqStreamingItemReader<OpLogRecord, OpLogItemRes> {
        val threadName = Thread.currentThread().name

println("opLogReader threadName>>> $threadName")
        // 解析 DTO
        val queryBO = objectMapper.readValue(queryJson, OpLogQueryBO::class.java)

        val condition = queryBO.params?.let { queryProvider.buildCondition(it) }
        val orderBy = queryProvider.buildOrderBy(queryBO.sort)

        // 2. 构建 jOOQ 查询（注意：不要在这里 fetch）
        val query = dsl.selectFrom(OP_LOG)
            .where(condition)
            .orderBy(orderBy)

        // 3. 返回自定义 Reader
        return JooqStreamingItemReader(query) { record ->
            record.into(OpLogItemRes::class.java) // 自动映射到 DTO
        }
    }

    @Bean
    fun opLogExcelWriter(): ExcelStreamingItemWriter<OpLogItemRes> {
        val tempFile = Files.createTempFile("oplog-", ".xlsx").toFile()
        return ExcelStreamingItemWriter(
            outputFilePath = tempFile.absolutePath,
            columnHeaders = listOf("用户ID", "用户名", "邮箱地址"),
            rowMapper = { record, row ->
                row.createCell(0).setCellValue(record.id.toString())
                row.createCell(1).setCellValue(record.method)
                row.createCell(2).setCellValue(record.uri)
            }
        )
    }

}