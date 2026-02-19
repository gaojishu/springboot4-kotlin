package com.example.api.admin.batch

import com.example.api.admin.event.ExportFinishedEvent
import com.example.base.provider.storage.StorageProvider
import com.example.base.utils.log
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.job.JobExecution
import org.springframework.batch.core.listener.JobExecutionListener
import org.springframework.batch.core.repository.JobRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.io.File

@Component
class ExportListener(
    private val storageProvider: StorageProvider,
    private val jobRepository: JobRepository,
    private val eventPublisher: ApplicationEventPublisher
) : JobExecutionListener {


    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status == BatchStatus.COMPLETED) {
            // 1. 获取所有的 StepExecution（一个 Job 可能包含多个 Step）
            val stepExecution = jobExecution.stepExecutions.first()

            // 2. 获取成功写入的行数
            val writeCount = stepExecution.writeCount

            // 从 StepContext 或 JobParameters 中获取文件路径
            val filePath = jobExecution.stepExecutions.first().executionContext.getString("outputFilePath")
            val file = File(filePath)

            if (file.exists()) {
                try {
                    val key = storageProvider.upload(file)
                    // 将上传后的 URL 存入 JobContext，方便 Controller 获取
                    jobExecution.executionContext.putString("result", key)
                    jobRepository.updateExecutionContext(jobExecution)  // 显式更新到数据库
                    log.info("File uploaded successfully. URL: $key")
                    val username = jobExecution.jobParameters.getString("username").toString()
//                    val adminId = jobExecution.jobParameters.getLong("adminId")

                    eventPublisher.publishEvent(ExportFinishedEvent(
                        username =  username,
                        attachments = listOf(key),
                        rowCount = writeCount,
                        title = "导出日志",
                        content = "共导出 $writeCount 行数据，请前往消息中心查看下载"
                    ))

                }catch (e: Exception){
                    log.error("File upload failed: ${e.message}")
                } finally {
                    file.delete() // 务必清理本地临时文件
                }
            }
        }
    }
}
